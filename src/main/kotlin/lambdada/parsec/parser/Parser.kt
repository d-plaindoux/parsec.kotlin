package lambdada.parsec.parser

import lambdada.parsec.extension.fold
import lambdada.parsec.extension.toInt
import lambdada.parsec.io.Reader
import java.util.*

typealias ParseFn<A> = (Reader) -> Response<A>

//
// Parser class providing pseudo-Monadic ADT
//

class Parser<A>(val parse: ParseFn<A>) {

    infix fun <B> map(f: (A) -> B): Parser<B> = Parser { s -> parse(s).map(f) }

    infix fun <B> flatMap(f: (A) -> Parser<B>): Parser<B> =
            Parser { s ->
                parse(s).fold(
                        { a ->
                            f(a.value).parse(a.input).fold(
                                            { b -> Accept(b.value, b.input, a.consumed || b.consumed) },
                                            { r -> Reject<B>(a.consumed || r.consumed) }
                                    )
                        },
                        { r -> Reject(r.consumed) }
                )
            }
}

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> =
        Parser { Accept(v, it, false) }

fun <A> fails(): Parser<A> =
        Parser { Reject<A>(false) }

fun <B> lazy(f: () -> Parser<B>): Parser<B> =
        Parser { f().parse(it) }

//
// Filtering
//

infix fun <A> Parser<A>.filter(p: (A) -> Boolean): Parser<A> = this flatMap { if (p(it)) returns(it) else fails() }

//
// Flow
//

// NOTE: do { ... } comprehension should be better
infix fun <A, B> Parser<A>.then(f: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> f map { Pair(a, it) } }

infix fun <A> Parser<A>.or(f: Parser<A>): Parser<A> =
        Parser { s ->
            parse(s).fold<Response<A>>(
                    { a -> a },
                    { r ->
                        if (r.consumed) {
                            r
                        } else {
                            f.parse(s)
                        }
                    }
            )
        }
//
// Element parser
//

val any: Parser<Char> =
        Parser { s -> s.getChar().fold({ Accept(it.first, it.second, true) }, { Reject<Char>(false) }) }

val eos: Parser<Unit> =
        any then fails<Unit>() map { Unit } or returns(Unit)

//
// Backtrack parser
//

fun <A> doTry(p: Parser<A>): Parser<A> =
        Parser { s -> p.parse(s).fold({ it }, { Reject<A>(false) }) }

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

fun char(c: Char): Parser<Char> = any filter { c == it }

fun charIn(s: CharRange): Parser<Char> = any filter { s.contains(it) }

fun charIn(s: String): Parser<Char> = any filter { s.contains(it) }

//
// Integer parser
//

val integer: Parser<Int> = charIn("-+") then rep(charIn('0'..'9')) map { (s, n) -> (listOf(s) + n).toInt() }

// etc.