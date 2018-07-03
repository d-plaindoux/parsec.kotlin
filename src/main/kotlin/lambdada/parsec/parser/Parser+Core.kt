package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Basic parsers
//

fun <I, A> returns(v: A): Parser<I, A> = { Accept(v, it, false) }

fun <I, A> fails(): Parser<I, A> = { Reject(it.location(), false) }

//
// Element parser
//

fun <I> any(): Parser<I, I> = {
    val a = it.read()
    when (a) {
        null -> Reject(it.location(), false)
        else -> Accept(a.first, a.second, true)
    }
}

//
// Negation
//

fun <I> not(p: Parser<I, I>): Parser<I, I> = { reader ->
    p(reader).fold({ fails<I, I>()(reader) }, { any<I>()(reader) })
}

//
// Lazy parser
//

fun <I, A> lazy(f: () -> Parser<I, A>): Parser<I, A> = { f()(it) }

//
// Backtracking
//

fun <I, A> doTry(p: Parser<I, A>): Parser<I, A> = { p(it).fold({ it }, { Reject(it.location, false) }) }

//
// Lookahead / Breaks ll(1) limitation
//

fun <I, A> lookahead(p: Parser<I, A>): Parser<I, A> =
        { reader -> p(reader).fold({ Accept(it.value, reader, false) }, { Reject(it.location, false) }) }
