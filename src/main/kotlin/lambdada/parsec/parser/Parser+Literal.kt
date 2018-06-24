package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.stringsToString
import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

//
// Specific Char parsers
//

fun char(c: Char): Parser<Char> =
        doTry(any satisfy { c == it })

fun charIn(c: CharRange): Parser<Char> =
        doTry(any satisfy { c.contains(it) })

fun charIn(s: String): Parser<Char> =
        doTry(any satisfy { s.contains(it) })

//
// Negation
//

fun not(p: Parser<Char>): Parser<Char> = Parser {
    val a = p.invoke(it)
    when (a) {
        is Reject -> any.invoke(it)
        is Accept -> fails<Char>().invoke(it)
    }
}

//
// Characters parser
//

fun string(s: String): Parser<String> =
        s.fold(returns(Unit), { a, c -> a thenLeft char(c) }) map { s }

fun delimitedString(): Parser<String> {
    val any: Parser<String> = doTry(string("\\\"")) or (not(char('"')) map { it.toString() })
    return char('"') thenRight optRep(any) thenLeft char('"') map { it.stringsToString() }
}

//
// Number parser
//

private val STRING_NUMBER: Parser<List<Char>> =
        rep(charIn('0'..'9'))

private val STRING_INTEGER: Parser<List<Char>> =
        opt(charIn("-+")) map { it ?: '+' } then STRING_NUMBER map { (s, n) -> (listOf(s) + n) }

val INTEGER: Parser<Int> =
        STRING_INTEGER map { it.charsToInt() }

val FLOAT: Parser<Float> =
        STRING_INTEGER then (opt(char('.') then STRING_NUMBER map { (s, n) -> (listOf(s) + n) }) map {
            it ?: listOf()
        }) map { (s, n) -> (s + n).charsToFloat() }
