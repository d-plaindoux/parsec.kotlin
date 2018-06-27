package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Sequence
// NOTE: [do] comprehension should be better

infix fun <A, B> Parser<A>.then(p: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> p map { b -> a to b } }

//
// Alternate Then
//

infix fun <A, B> Parser<A>.thenLeft(p: Parser<B>): Parser<A> =
        this then p map { it.first }

infix fun <A, B> Parser<A>.thenRight(p: Parser<B>): Parser<B> =
        this then p map { it.second }

//
// Choice
//

infix fun <A> Parser<A>.or(p: Parser<A>): Parser<A> = Parser { reader ->
    val a = this.parse(reader)
    when (a.consumed) {
        true -> a
        false ->
            when (a) {
                is Accept -> a
                is Reject -> p.parse(reader)
            }
    }
}


//
// Kleene operator, optional
//

fun <A> opt(p: Parser<A>): Parser<A?> =
        p map { it as A? } or returns<A?>(null)

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

//private tailrec fun <A> optRep(p: Parser<A>, acc: List<A>, consumed: Boolean, charReader: CharReader): Response<List<A>> {
//    val a = p.parse(charReader)
//    return when (a) {
//        is Reject -> Accept(acc, charReader, consumed)
//        is Accept -> optRep(p, acc + a.value, consumed || a.consumed, a.input)
//    }
//}

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        opt(p then lazy { optRep(p) }) map { it?.let { listOf(it.first) + it.second } ?: listOf() }
// Parser { optRep(p, listOf(), false, it) }

fun <A> rep(p: Parser<A>): Parser<List<A>> =
        p then optRep(p) map { r -> listOf(r.first) + r.second }

//
// End of stream
//

var eos: Parser<Unit> =
        any thenRight fails<Unit>() or returns(Unit)
