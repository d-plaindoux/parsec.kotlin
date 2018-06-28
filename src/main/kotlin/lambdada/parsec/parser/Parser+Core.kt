package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> = { reader -> Accept(v, reader, false) }

fun <A> fails(): Parser<A> = { reader -> Reject<A>(reader.location(), false) }

//
// Element parser
//

var any: Parser<Char> = { reader ->
    when (reader.canRead()) {
        true -> {
            val a = reader.read()
            Accept(a.first, a.second, true)
        }
        false -> Reject<Char>(reader.location(), false)
    }
}

//
// Lazy parser
//

fun <A> lazy(f: () -> Parser<A>): Parser<A> = { reader -> f().invoke(reader) }

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> = { reader ->
    val a = p.invoke(reader)
    when (a) {
        is Accept -> a
        is Reject -> Reject<A>(a.location, false)
    }
}

//
// Lookahead / Breaks ll(1) limitation
//

fun <A> lookahead(p: Parser<A>): Parser<A> = { reader ->
    val a = p.invoke(reader)
    when (a) {
        is Accept -> Accept(a.value, reader, false)
        is Reject -> Reject<A>(a.location, false)
    }
}