package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Parser providing pseudo-Monadic ADT
//

infix fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> =
        Parser<B> { input ->
            val a = this.invoke(input)
            when (a) {
                is Reject -> Reject(a.position, a.consumed)
                is Accept -> {
                    val b = f(a.value).invoke(a.input)
                    when (b) {
                        is Reject -> Reject(b.position, a.consumed || b.consumed)
                        is Accept -> Accept(b.value, b.input, a.consumed || b.consumed)
                    }
                }
            }
        }

infix fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> =
        this flatMap { returns(f(it)) }


//
// Filtering (not greedy)
//

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> =
        this flatMap { if (p(it)) returns(it) else fails() }

//
// Applicative context sensitive
//

//infix fun <A, B> Parser<A>.wrong_applicative(f: Parser<(A) -> B>): Parser<B> =
//        f flatMap { this map it }

infix fun <A, B> Parser<A>.applicative(f: Parser<(A) -> B>): Parser<B> =
        this flatMap { f map { f -> f(it) } }

//
// Kliesli monads pipelining
//

infix fun <A, B, C> ((A) -> Parser<B>).and(f: (B) -> Parser<C>): (A) -> Parser<C> =
        { this(it) flatMap f }
