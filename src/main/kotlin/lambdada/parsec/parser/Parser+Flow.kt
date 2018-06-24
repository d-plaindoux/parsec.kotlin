package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Flow
//


// NOTE: [do] comprehension should be better
infix fun <A, B> Parser<A>.then(f: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> f map { b -> a to b } }

infix fun <A> Parser<A>.or(f: Parser<A>): Parser<A> =
        Parser { reader ->
            val a = invoke(reader)
            when (a) {
                is Reject ->
                    when (a.consumed) {
                        true -> a // Not commutative
                        false -> f.invoke(reader)
                    }
                is Accept -> a
            }
        }

//
// Alternate Then
//

infix fun <A, B> Parser<A>.thenLeft(f: Parser<B>): Parser<A> =
        this then f map { it.first }

infix fun <A, B> Parser<A>.thenRight(f: Parser<B>): Parser<B> =
        this then f map { it.second }

//
// Kleene operator, optional
//

fun <A> opt(p: Parser<A>): Parser<A?> =
        p map { it as A? } or returns<A?>(null)

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

fun <A> optRep(p: Parser<A>, acc: List<A>): Parser<List<A>> =
        p flatMap { a -> optRep(p, acc + a) } or returns(acc)

tailrec fun <A> optRep(p: Parser<A>, acc: List<A>, consumed: Boolean, reader: Reader): Response<List<A>> {
    val nr = p.invoke(reader)
    return when (nr) {
        is Reject -> Accept(acc, reader, consumed)
        is Accept -> optRep(p, acc + nr.value, consumed || nr.consumed, nr.input)
    }
}

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        // opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it ?: listOf() }
        // optRep(p, arrayListOf())
        Parser { optRep(p, arrayListOf(), false, it) }

fun <A> rep(p: Parser<A>): Parser<List<A>> =
        optRep(p) satisfy { it.isNotEmpty() }

//
// End of stream
//

var eos: Parser<Unit> =
        any thenRight fails<Unit>() or returns(Unit)
