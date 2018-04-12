package lambdada.parsec.parser

import lambdada.parsec.extension.fold
import lambdada.parsec.extension.next
import lambdada.parsec.extension.toInt
import lambdada.parsec.io.Reader
import java.util.*

//
// Parser type definition
//

typealias Parser<A> = (Reader) -> Response<A>

//
// Parser providing pseudo-Monadic ADT
//

infix fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> =
        { reader -> this.invoke(reader).map(f) } // Similar to Object#apply in Scala

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

//
// Basic parsers
//

fun <A> returns(v: A, consumed: Boolean = false): Parser<A> =
        { Accept(v, it, consumed) }

fun <A> fails(consumed: Boolean = false): Parser<A> =
        { Reject(it.offset, consumed) }

fun <B> lazy(f: () -> Parser<B>): Parser<B> =
        { f()(it) }

//
// Flow
//

// NOTE: [do] comprehension should be better
infix fun <A, B> Parser<A>.then(f: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> f map { b -> Pair(a, b) } }

infix fun <A> Parser<A>.or(f: Parser<A>): Parser<A> =
        { s ->
            this(s).fold<Response<A>>(
                    { it },
                    { r ->
                        when (r.consumed) {
                            true -> r
                            false -> f(s)
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
// Backtrack parser
//

fun <A> doTry(p: Parser<A>): Parser<A> =
        { r -> p(r).fold({ it }, { fails<A>(false)(r) }) }

//
// Filtering
//

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> =
        this flatMap { if (p(it)) returns(it) else fails() }

//
// Kleene operator, optional
//

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

fun <A> opt(p: Parser<A>): Parser<Optional<A>> =
        p.map { Optional.of(it) } or returns(Optional.empty())

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it.orElse(listOf()) }

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
        s.next().fold(
                { (c, sp) ->
                    sp.fold(char(c), { p, c -> p thenRight char(c) }) thenRight returns(s, true)
                },
                { returns(s) }
        )

//
// Integer parser
//

val integer: Parser<Int> =
        opt(charIn("-+")) map { it.orElse('+') } then rep(charIn('0'..'9')) map { (s, n) -> (listOf(s) + n).toInt() }
