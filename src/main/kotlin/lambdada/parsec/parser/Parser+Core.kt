package lambdada.parsec.parser

import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> = Parser { reader -> Accept(v, reader, false) }

fun <A> fails(): Parser<A> = Parser { reader -> Reject<A>(reader.location(), false) }

//
// Element parser
//

var any: Parser<Char> = Parser { reader ->
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

fun <A> lazy(f: () -> Parser<A>): Parser<A> = Parser { reader -> f().invoke(reader) }

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> = Parser { reader ->
    val a = p.invoke(reader)
    when (a) {
        is Accept -> a
        is Reject -> Reject<A>(a.location, false)
    }
}

//
// Lookahead / Breaks ll(1) limitation
//

fun <A> lookahead(p: Parser<A>): Parser<A> = Parser { reader ->
    val a = p.invoke(reader)
    when (a) {
        is Accept -> Accept(a.value, reader, false)
        is Reject -> Reject<A>(a.location, false)
    }
}