package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Basic parsers
//

fun <A> returns(v: A, consumed: Boolean = false): Parser<A> =
        Parser { Accept(v, it, consumed) }

fun <A> fails(consumed: Boolean = false): Parser<A> =
        Parser { Reject<A>(it.position, consumed) }

//
// Element parser
//

var any: Parser<Char> = Parser {
    val (c, input) = it.next()
    when (c) {
        null -> fails<Char>(false).invoke(it)
        else -> returns(c, true).invoke(input)
    }
}

//
// Lazy parser
//

fun <A> lazy(f: () -> Parser<A>): Parser<A> =
        Parser { f().invoke(it) }

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> = Parser {
    val a = p.invoke(it)
    when (a) {
        is Accept -> a
        is Reject -> fails<A>().invoke(it)
    }
}

//
// Lookahead
//

fun <A> lookahead(p: Parser<A>): Parser<A> = Parser {
    val a = p.invoke(it)
    when (a) {
        is Accept -> returns(a.value, false).invoke(it)
        is Reject -> fails<A>().invoke(it)
    }
}
