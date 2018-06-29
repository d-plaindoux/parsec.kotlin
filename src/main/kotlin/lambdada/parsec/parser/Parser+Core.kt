package lambdada.parsec.parser

//
// Basic parsers
//

fun <A> returns(v: A): Parser<A> = TODO()

fun <A> fails(): Parser<A> = TODO()

//
// Element parser
//

var any: Parser<Char> = TODO()

//
// Lazy parser
//

fun <A> lazy(f: () -> Parser<A>): Parser<A> = TODO()

//
// Backtracking
//

fun <A> doTry(p: Parser<A>): Parser<A> = TODO()

//
// Lookahead / Breaks ll(1) limitation
//

fun <A> lookahead(p: Parser<A>): Parser<A> = TODO()