package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.stringsToString
import lambdada.parsec.io.Reader

//
// Parser type definition
//

typealias Parser<T, A> = (Reader<T>) -> Response<T, A>
typealias CharParser<A> = Parser<Char, A>

//
// Basic parsers
//

fun <A> returns(v: A, consumed: Boolean = false): CharParser<A> =
        { Accept(v, it, consumed) }

fun <A> fails(consumed: Boolean = false): CharParser<A> =
        { Reject(it.offset, consumed) }

//
// Parser providing pseudo-Monadic ADT
//

infix fun <A, B> CharParser<A>.flatMap(f: (A) -> CharParser<B>): CharParser<B> =
        { input ->
            val a = this(input)
            when (a) {
                is Accept -> {
                    val b = f(a.value)(a.input)
                    when (b) {
                        is Accept -> Accept(b.value, b.input, a.consumed || b.consumed)
                        is Reject -> Reject(b.position, a.consumed || b.consumed)
                    }
                }
                is Reject -> Reject(a.position, a.consumed)
            }
        }

infix fun <A, B> CharParser<A>.map(f: (A) -> B): CharParser<B> =
        this flatMap { returns(f(it)) }

//
// Flow
//

// NOTE: [do] comprehension should be better
infix fun <A, B> CharParser<A>.then(f: CharParser<B>): CharParser<Pair<A, B>> =
        this flatMap { a -> f map { b -> a to b } }

infix fun <A> CharParser<A>.or(f: CharParser<A>): CharParser<A> =
        { reader ->
            val a = this(reader)
            when (a) {
                is Accept -> a
                is Reject ->
                    when (a.consumed) {
                        true -> a // Not commutative
                        false -> f(reader)
                    }
            }
        }

//
// Alternate Then
//

infix fun <A, B> CharParser<A>.thenLeft(f: CharParser<B>): CharParser<A> =
        this then f map { it.first }

infix fun <A, B> CharParser<A>.thenRight(f: CharParser<B>): CharParser<B> =
        this then f map { it.second }

//
// Element parser
//

var any: CharParser<Char> = {
    val (c, input) = it.next()
    when (c) {
        null -> fails<Char>(false)(it)
        else -> returns(c, true)(input)
    }
}

var eos: CharParser<Unit> =
        any thenRight fails<Unit>() or returns(Unit)

//
// Filtering
//

infix fun <A> CharParser<A>.satisfy(p: (A) -> Boolean): CharParser<A> =
        this flatMap { if (p(it)) returns(it) else fails() }

//
// Lazy parser
//

fun <A> lazy(f: () -> CharParser<A>): CharParser<A> =
        { f()(it) }

//
// Kleene operator, optional
//

fun <A> opt(p: CharParser<A>): CharParser<A?> =
        p map { it as A? } or returns<A?>(null)

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

tailrec fun <A> occurrence(p: CharParser<A>, min: Int, r: Accept<Char, List<A>>): Response<Char, List<A>> {
    val nr = p(r.input)
    return when (nr) {
        is Accept ->
            occurrence(p, min, Accept(r.value + nr.value, nr.input, r.consumed || nr.consumed))
        is Reject -> if (r.value.size >= min) {
            r
        } else {
            Reject(nr.position, r.consumed || nr.consumed)
        }
    }
}

fun <A> optRep(p: CharParser<A>): CharParser<List<A>> = {
    // opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it ?: listOf() }
    occurrence(p, 0, Accept(arrayListOf(), it, false))
}

fun <A> rep(p: CharParser<A>): CharParser<List<A>> = {
    // return p then optRep(p) map { (a, b) -> listOf(a) + b }
    occurrence(p, 1, Accept(arrayListOf(), it, false))
}


//
// Backtracking
//

fun <A> doTry(p: CharParser<A>): CharParser<A> = {
    val a = p(it)
    when (a) {
        is Accept -> a
        is Reject -> Reject(a.position, false)
    }
}

//
// Lookahead
//

fun <A> lookahead(p: CharParser<A>): CharParser<A> = {
    val a = p(it)
    when (a) {
        is Accept -> returns(a.value, false)(it)
        is Reject -> fails<A>()(it)
    }
}

//
// Negation
//

fun not(p: CharParser<Char>): CharParser<Char> = {
    val a = p(it)
    when (a) {
        is Accept -> fails<Char>()(it)
        is Reject -> any(it)
    }
}

//
// Specific Char parsers
//

fun char(c: Char): CharParser<Char> =
        doTry(any satisfy { c == it })

fun charIn(s: CharRange): CharParser<Char> =
        doTry(any satisfy { s.contains(it) })

fun charIn(s: String): CharParser<Char> =
        doTry(any satisfy { s.contains(it) })

//
// Characters parser
//

fun string(s: String): CharParser<String> =
        s.fold(returns(Unit), { a, c -> a thenLeft char(c) }) map { s }

fun delimitedString(): CharParser<String> {
    val anyChar: CharParser<String> = doTry(string("\\\"")) or (not(char('"')) map { it.toString() })
    return char('"') thenRight optRep(anyChar) thenLeft char('"') map { it.stringsToString() }
}

//
// Number parser
//

private val stringNumber: CharParser<List<Char>> =
        rep(charIn('0'..'9'))

private val stringInteger: CharParser<List<Char>> =
        opt(charIn("-+")) map { it ?: '+' } then stringNumber map { (s, n) -> (listOf(s) + n) }

val integer: CharParser<Int> =
        stringInteger map { it.charsToInt() }

val float: CharParser<Float> =
        stringInteger then (opt(char('.') then stringNumber map { (s, n) -> (listOf(s) + n) }) map {
            it ?: listOf()
        }) map { (s, n) -> (s + n).charsToFloat() }

//
// Applicative context sensitive
//

infix fun <A, B> CharParser<A>.wrong_applicative(f: CharParser<(A) -> B>): CharParser<B> =
        f flatMap { this map it }

infix fun <A, B> CharParser<A>.applicative(f: CharParser<(A) -> B>): CharParser<B> =
        this flatMap { f map { f -> f(it) } }

//
// Kliesli monads pipelining
//

infix fun <A, B, C> ((A) -> CharParser<B>).kliesli(f: (B) -> CharParser<C>): (A) -> CharParser<C> =
        { this(it) flatMap f }
