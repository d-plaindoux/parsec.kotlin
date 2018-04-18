package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.fold
import lambdada.parsec.extension.stringsToString
import lambdada.parsec.io.Reader

//
// Parser type definition
//

typealias Parser<T, A> = (Reader<T>) -> Response<T, A>

//
// Basic parsers
//

fun <T, A> returns(v: A, consumed: Boolean = false): Parser<T, A> =
        { Accept(v, it, consumed) }

fun <T, A> fails(consumed: Boolean = false): Parser<T, A> =
        { Reject(it.offset, consumed) }

//
// Parser providing pseudo-Monadic ADT
//

infix fun <T, A, B> Parser<T, A>.flatMap(f: (A) -> Parser<T, B>): Parser<T, B> =
        { reader ->
            this(reader).fold(
                    { a ->
                        f(a.value)(a.input).fold(
                                { b -> Accept(b.value, b.input, a.consumed || b.consumed) },
                                { r -> Reject(r.position, a.consumed || r.consumed) }
                        )
                    },
                    { r -> Reject(r.position, r.consumed) }
            )
        }

infix fun <T, A, B> Parser<T, A>.map(f: (A) -> B): Parser<T, B> =
        this flatMap { returns<T, B>(f(it)) }

//
// Flow
//

// NOTE: [do] comprehension should be better
infix fun <T, A, B> Parser<T, A>.then(f: Parser<T, B>): Parser<T, Pair<A, B>> =
        this flatMap { a -> f map { b -> a to b } }

infix fun <T, A> Parser<T, A>.or(f: Parser<T, A>): Parser<T, A> =
        { reader ->
            this(reader).fold<Response<T, A>>(
                    { it },
                    { r ->
                        when (r.consumed) {
                            true -> r // Not commutative
                            false -> f(reader)
                        }
                    }
            )
        }

//
// Alternate Then
//

infix fun <T, A, B> Parser<T, A>.thenLeft(f: Parser<T, B>): Parser<T, A> =
        this then f map { it.first }

infix fun <T, A, B> Parser<T, A>.thenRight(f: Parser<T, B>): Parser<T, B> =
        this then f map { it.second }

//
// Element parser
//

fun <T> any(): Parser<T, T> =
        { r -> r.next().fold({ returns<T, T>(it.first, true)(it.second) }, { fails<T, T>(false)(r) }) }

fun <T> eos(): Parser<T, Unit> =
        any<T>() then fails<T, Unit>() map { Unit } or returns(Unit)

//
// Filtering
//

infix fun <T, A> Parser<T, A>.satisfy(p: (A) -> Boolean): Parser<T, A> =
        this flatMap { if (p(it)) returns<T, A>(it) else fails() }

//
// Lazy parser
//

fun <T, A> lazy(f: () -> Parser<T, A>): Parser<T, A> =
        { f()(it) }

//
// Kleene operator, optional
//

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

fun <T, A> opt(p: Parser<T, A>): Parser<T, A?> =
        p map { it as A? } or returns<T, A?>(null)

private fun <T, A> occurrence(p: Parser<T, A>, min: Int = 0, max: Int = Int.MAX_VALUE): Parser<T, List<A>> = {
    val value = arrayListOf<A>() // o_O
    var input = it
    var consumed = false

    while (value.size <= max &&
            p(input).fold({
                value.add(it.value)
                input = it.input
                consumed = it.consumed
                true
            }, {
                false
            }));

    if (min <= value.size) {
        Accept(value, input, consumed)
    } else {
        Reject(input.offset, consumed)
    }
}

fun <T, A> optRep(p: Parser<T, A>): Parser<T, List<A>> =
        occurrence(p, min = 0)
        // opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it ?: listOf() }

fun <T, A> rep(p: Parser<T, A>): Parser<T, List<A>> =
        occurrence(p, min = 1)
        // p then optRep(p) map { (a, b) -> listOf(a) + b }

//
// Backtracking
//

fun <T, A> doTry(p: Parser<T, A>): Parser<T, A> =
        { r -> p(r).fold({ it }, { fails<T, A>(false)(r) }) }

//
// Lookahead
//

fun <T, A> lookahead(p: Parser<T, A>): Parser<T, A> =
        { r -> p(r).fold({ returns<T, A>(it.value)(r) }, { fails<T, A>(false)(r) }) }

//
// Negation
//

fun <A> not(p: Parser<A, A>): Parser<A, A> =
        { r -> p(r).fold({ fails<A, A>(false)(r) }, { any<A>()(r) }) }

//
// Specific Char parsers
//

fun char(c: Char): Parser<Char, Char> = doTry(any<Char>() satisfy { c == it })

fun charIn(s: CharRange): Parser<Char, Char> = doTry(any<Char>() satisfy { s.contains(it) })

fun charIn(s: String): Parser<Char, Char> = doTry(any<Char>() satisfy { s.contains(it) })

//
// Characters parser
//

fun string(s: String): Parser<Char, String> =
        s.fold(returns<Char, StringBuilder>(StringBuilder()), { a, c -> a then char(c) map { (s, c) -> s.append(c) } }) map { it.toString() }

fun delimitedString(): Parser<Char, String> {
    val anyChar: Parser<Char, String> = doTry(string("\\\"")) or (not(char('"')) map { it.toString() })
    return char('"') thenRight optRep(anyChar) thenLeft char('"') map { it.stringsToString() }
}

//
// Number parser
//

private val stringNumber: Parser<Char, List<Char>> =
        rep(charIn('0'..'9'))

private val stringInteger: Parser<Char, List<Char>> =
        opt(charIn("-+")) map { it ?: '+' } then stringNumber map { (s, n) -> (listOf(s) + n) }

val integer: Parser<Char, Int> =
        stringInteger map { it.charsToInt() }

val float: Parser<Char, Float> =
        stringInteger then (opt(char('.') then stringNumber map { (s, n) -> (listOf(s) + n) }) map {
            it ?: listOf()
        }) map { (s, n) -> (s + n).charsToFloat() }

//
// Applicative context sensitive
//

infix fun <T, A, B> Parser<T, A>.wrong_applicative(f: Parser<T, (A) -> B>): Parser<T, B> =
        f flatMap { this map it }

infix fun <T, A, B> Parser<T, A>.applicative(f: Parser<T, (A) -> B>): Parser<T, B> =
        this flatMap { f map { f -> f(it) } }

//
// Kliesli monads pipelining
//

infix fun <T, A, B, C> ((A) -> Parser<T, B>).kliesli(f: (B) -> Parser<T, C>): (A) -> Parser<T, C> =
        { this(it) flatMap f }
