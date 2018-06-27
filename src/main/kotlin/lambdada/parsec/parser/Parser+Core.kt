package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> =
        Parser { Accept(v, it, false) }

fun <A> fails(): Parser<A> =
        Parser { Reject<A>(it.location(), false) }

//
// Element parser
//

var any: Parser<Char> = Parser {
    when (it.canRead()) {
        true -> {
            val (c, input) = it.read()
            Accept(c, input, true)
        }
        false -> Reject<Char>(it.location(), false)
    }
}

//
// Lazy parser
//

fun <A> lazy(f: () -> Parser<A>): Parser<A> =
        Parser { f().parse(it) }

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> = Parser {
    val a = p.parse(it)
    when (a) {
        is Accept -> a
        is Reject -> Reject<A>(it.location(), false)
    }
}

//
// Lookahead / Breaks ll(1) limitation
//

fun <A> lookahead(p: Parser<A>): Parser<A> = Parser {
    val a = p.parse(it)
    when (a) {
        is Accept -> Accept(a.value, it, false)
        is Reject -> Reject<A>(it.location(), false)
    }
}
