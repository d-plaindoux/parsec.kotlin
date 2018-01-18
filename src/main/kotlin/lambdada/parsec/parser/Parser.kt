package lambdada.parsec.parser

import lambdada.parsec.extension.fold
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
        { this.invoke(it).map(f) }

infix fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> =
        {
            this(it).fold(
                    { a ->
                        f(a.value)(a.input).fold(
                                { b -> Accept(b.value, b.input, a.consumed || b.consumed) },
                                { r -> Reject(a.consumed || r.consumed) }
                        )
                    },
                    { r -> Reject(r.consumed) }
            )
        }

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> =
        { Accept(v, it, false) }

fun <A> fails(): Parser<A> =
        { Reject(false) }

fun <B> lazy(f: () -> Parser<B>): Parser<B> =
        { f()(it) }

//
// Filtering
//

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> =
        doTry(this flatMap { if (p(it)) returns(it) else fails() })

//
// Flow
//

// NOTE: do { ... } comprehension should be better
infix fun <A, B> Parser<A>.then(f: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> f map { Pair(a, it) } }

infix fun <A> Parser<A>.or(f: Parser<A>): Parser<A> =
        { s ->
            this(s).fold<Response<A>>(
                    { a -> a },
                    { r ->
                        if (r.consumed) {
                            r
                        } else {
                            f(s)
                        }
                    }
            )
        }
//
// Element parser
//

val any: Parser<Char> =
        { it.getChar().fold({ Accept(it.first, it.second, true) }, { Reject(false) }) }

val eos: Parser<Unit> =
        any then fails<Unit>() map { Unit } or returns(Unit)

//
// Backtrack parser
//

fun <A> doTry(p: Parser<A>): Parser<A> =
        { p(it).fold({ it }, { Reject(false) }) }

//
// Kleene operator, optional
//

// NOTE: Greedy parsers

fun <A> opt(p: Parser<A>): Parser<Optional<A>> =
        p.map { Optional.of(it) } or returns(Optional.empty())

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it.orElse(listOf()) }

fun <A> rep(p: Parser<A>): Parser<List<A>> =
        p then optRep(p) map { (a, b) -> listOf(a) + b }

//
// Specific Char parsers
//

fun char(c: Char): Parser<Char> = any satisfy { c == it }

fun charIn(s: CharRange): Parser<Char> = any satisfy { s.contains(it) }

fun charIn(s: String): Parser<Char> = any satisfy { s.contains(it) }

//
// Integer parser
//

val integer: Parser<Int> = charIn("-+") then rep(charIn('0'..'9')) map { (s, n) -> (listOf(s) + n).toInt() }

// etc.