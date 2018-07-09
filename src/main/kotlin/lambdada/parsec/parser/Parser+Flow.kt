package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Sequence
// NOTE: [do] comprehension should be better

infix fun <I, A, B> Parser<I, A>.then(p: Parser<I, B>): Parser<I, Pair<A, B>> = this flatMap { a -> p map { a to it } }

//
// Alternate Then
//

fun <I, A, B> Parser<I, Pair<A, B>>.first(): Parser<I, A> = this map { it.first }

infix fun <I, A, B> Parser<I, A>.thenLeft(p: Parser<I, B>): Parser<I, A> = (this then p).first()

fun <I, A, B> Parser<I, Pair<A, B>>.second(): Parser<I, B> = this map { it.second }

infix fun <I, A, B> Parser<I, A>.thenRight(p: Parser<I, B>): Parser<I, B> = (this then p).second()

//
// Choice
//

infix fun <I, A> Parser<I, A>.or(p: Parser<I, A>): Parser<I, A> = { reader ->
    val a = this(reader)
    when (a.consumed) {
        true -> a
        false -> a.fold({ a }, { p(reader) })
    }
}

//
// Kleene operator, optional
//

val <I, A> Parser<I, A>.opt: Parser<I, A?> get() = this map { it as A? } or returns<I, A?>(null)

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

tailrec fun <I, A> occurrences(p: Parser<I, A>, acc: List<A>, consumed: Boolean, reader: Reader<I>): Response<I, List<A>> {
    val a = p(reader)
    return when (a) {
        is Reject -> Accept(acc, reader, consumed)
        is Accept -> occurrences(p, acc + a.value, consumed || a.consumed, a.input)
    }
}

val <I, A> Parser<I, A>.optRep: Parser<I, List<A>> get() = { occurrences(this, listOf(), false, it) }

val <I, A> Parser<I, A>.rep: Parser<I, List<A>> get() = this then this.optRep map { listOf(it.first) + it.second }

//
// End of stream
//

fun <A> eos(): Parser<A, Unit> = {
    when (it.read()) {
        null -> Accept(Unit, it, false)
        else -> Reject(it.location(), false)
    }
}