package lambdada.parsec.parser

import lambdada.parsec.extension.pipe
import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Parser providing pseudo-Monadic ADT
//

infix fun <I, A, B> Parser<I, A>.map(f: (A) -> B): Parser<I, B> =
        { this(it).fold({ Accept(f(it.value), it.input, it.consumed) }, { Reject(it.location, it.consumed) }) }

fun <I, A> join(p: Parser<I, Parser<I, A>>): Parser<I, A> = {
    val a = p(it)
    when (a) {
        is Accept -> {
            val b = a.value.invoke(a.input)
            when (b) {
                is Reject -> Reject(b.location, b.consumed || a.consumed)
                is Accept -> Accept(b.value, b.input, b.consumed || a.consumed)
            }
        }
        is Reject -> Reject(a.location, a.consumed)
    }
}

infix fun <I, A, B> Parser<I, A>.flatMap(f: (A) -> Parser<I, B>): Parser<I, B> =
        join(this map f)

//
// Filtering
//

infix fun <I, A> Parser<I, A>.satisfy(p: (A) -> Boolean): Parser<I, A> =
        this flatMap {
            if (p(it)) {
                returns<I, A>(it)
            } else {
                fails()
            }
        }

//
// Applicative
// Warning: In the monadic combinator, the second parser always depends
//          on the result of the first parser.
//

infix fun <I, A, B> Parser<I, A>.applicative(p: Parser<I, (A) -> B>): Parser<I, B> =
        this flatMap { v -> p map { f -> f(v) } }

//
// Kliesli
//

infix fun <I, A, B, C> ((A) -> Parser<I, B>).then(p2: (B) -> Parser<I, C>): (A) -> Parser<I, C> =
        this pipe { it flatMap p2 }

