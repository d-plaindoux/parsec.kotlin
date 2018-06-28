package lambdada.parsec.parser

import lambdada.parsec.extension.pipe
import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Parser providing pseudo-Monadic ADT
//

// p.map(...)
infix fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> = { reader ->
    val a = this.invoke(reader)
    when (a) {
        is Reject -> Reject<B>(a.location, a.consumed)
        is Accept -> Accept(f(a.value), a.input, a.consumed)
    }
}

fun <A> join(p: Parser<Parser<A>>): Parser<A> = { reader ->
    val a: Response<Parser<A>> = p.invoke(reader)
    when (a) {
        is Reject -> Reject<A>(a.location, a.consumed)
        is Accept -> {
            val b = a.value.invoke(a.input)
            when (b) {
                is Reject -> Reject<A>(b.location, b.consumed || a.consumed)
                is Accept -> Accept(b.value, b.input, b.consumed || a.consumed)
            }
        }
    }
}

infix fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> =
        join(this map f)

//
// Filtering
//

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> =
        this flatMap {
            if (p(it)) {
                returns(it)
            } else {
                fails()
            }
        }

//
// Applicative
// Warning: In the monadic combinator, the second parser always depends
//          on the result of the first parser.
//

fun <A, B> Parser<A>.applicative(p: Parser<(A) -> B>): Parser<B> =
        this.flatMap { v -> p.map { f -> f(v) } }

//
// Kliesli
//

fun <A, B, C> ((A) -> Parser<B>).then(p2: (B) -> Parser<C>): (A) -> Parser<C> =
        this pipe { b -> b.flatMap(p2) }
