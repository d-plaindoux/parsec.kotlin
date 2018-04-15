package lambdada.parsec.parser

import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.fold
import lambdada.parsec.extension.stringsToString
import lambdada.parsec.io.Reader

//
// Parser type definition
//

typealias Parser<A> = (Reader) -> Response<A>

//
// Basic parsers
//

fun <A> returns(v: A, consumed: Boolean = false): Parser<A> =
        { Accept(v, it, consumed) }

fun <A> fails(consumed: Boolean = false): Parser<A> =
        { Reject(it.offset, consumed) }

//
// Parser providing pseudo-Monadic ADT
//

infix fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> =
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

infix fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> =
        this flatMap { returns(f(it)) }

//
// Applicative context sensitive
//

infix fun <A, B> Parser<A>.wrong_applicative(f: Parser<(A) -> B>): Parser<B> =
        f flatMap { this map it }

infix fun <A, B> Parser<A>.applicative(f: Parser<(A) -> B>): Parser<B> =
        this flatMap { f map { f -> f(it) } }

//
// Kliesli monads pipelining
//

infix fun <A, B, C> ((A) -> Parser<B>).pipe(f: (B) -> Parser<C>): (A) -> Parser<C> =
        { this(it) flatMap f }

//
// Flow
//

// NOTE: [do] comprehension should be better
infix fun <A, B> Parser<A>.then(f: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> f map { b -> a to b } }

infix fun <A> Parser<A>.or(f: Parser<A>): Parser<A> =
        { reader ->
            this(reader).fold<Response<A>>(
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

infix fun <A, B> Parser<A>.thenLeft(f: Parser<B>): Parser<A> =
        this then f map { it.first }

infix fun <A, B> Parser<A>.thenRight(f: Parser<B>): Parser<B> =
        this then f map { it.second }

//
// Element parser
//

val any: Parser<Char> =
        { r -> r.next().fold({ returns(it.first, true)(it.second) }, { fails<Char>(false)(r) }) }

val eos: Parser<Unit> =
        any then fails<Unit>() map { Unit } or returns(Unit)

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> =
        { r -> p(r).fold({ it }, { fails<A>(false)(r) }) }

//
// Negation
//

fun not(p: Parser<Char>): Parser<Char> =
        { r -> p(r).fold({ fails<Char>(false)(r) }, { any(r) }) }

//
// Filtering
//

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> =
        this flatMap { if (p(it)) returns(it) else fails() }

//
// Lazy parser
//

fun <B> lazy(f: () -> Parser<B>): Parser<B> =
        { f()(it) }

//
// Kleene operator, optional
//

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

fun <A> opt(p: Parser<A>): Parser<A?> =
        p map { it as A? } or returns<A?>(null)

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it ?: listOf() }

fun <A> rep(p: Parser<A>): Parser<List<A>> =
        p then optRep(p) map { (a, b) -> listOf(a) + b }

//
// Specific Char parsers
//

fun char(c: Char): Parser<Char> = doTry(any satisfy { c == it })

fun charIn(s: CharRange): Parser<Char> = doTry(any satisfy { s.contains(it) })

fun charIn(s: String): Parser<Char> = doTry(any satisfy { s.contains(it) })

//
// Characters parser
//

fun string(s: String): Parser<String> =
        s.fold(returns(StringBuilder()), { a, c -> a then char(c) map { (s, c) -> s.append(c) } }) map { it.toString() }

fun delimitedString(): Parser<String> {
    val anyChar: Parser<String> = char('\\') then char('"') map { "\\\"" } or (not(char('"')) map { it.toString() })
    return char('"') thenRight optRep(anyChar) thenLeft char('"') map { it.stringsToString() }
}

//
// Integer parser
//

val integer: Parser<Int> =
        opt(charIn("-+")) map { it ?: '+' } then rep(charIn('0'..'9')) map { (s, n) -> (listOf(s) + n).charsToInt() }
