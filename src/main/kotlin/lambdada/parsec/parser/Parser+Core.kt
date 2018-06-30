package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> = { Accept(v, it, false) }

fun <A> fails(): Parser<A> = { Reject(it.location(), false) }

//
// Element parser
//

var any: Parser<Char> = {
    when (it.canRead()) {
        true -> {
            val a = it.read()
            Accept(a.first, a.second, true)
        }
        false -> Reject(it.location(), false)
    }
}

//
// Lazy parser
//

fun <A> lazy(f: () -> Parser<A>): Parser<A> = { f()(it) }

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> = { p(it).fold({ it }, { Reject(it.location, false) }) }

//
// Lookahead / Breaks ll(1) limitation
//

fun <A> lookahead(p: Parser<A>): Parser<A> =
        { reader -> p(reader).fold({ Accept(it.value, reader, false) }, { Reject(it.location, false) }) }
