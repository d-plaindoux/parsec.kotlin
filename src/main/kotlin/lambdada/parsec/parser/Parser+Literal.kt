package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.stringsToString

//
// Specific Char parsers
//

fun char(c: Char): Parser<Char> = TODO()

fun charIn(c: CharRange): Parser<Char> = TODO()

fun charIn(s: String): Parser<Char> = TODO()

fun charIn(vararg s: Char): Parser<Char> = TODO()

//
// Negation
//

fun not(p: Parser<Char>): Parser<Char> = TODO()

//
// Characters parser
//

fun string(s: String): Parser<String> = TODO()

fun delimitedString(): Parser<String> {
    val charExceptColumn = doTry(string("\\\"")) or (not(char('"')).map(Char::toString))

    return (char('"') thenRight optRep(charExceptColumn) thenLeft char('"')).map { it.stringsToString() }
}

//
// Number parser
//

private val STRING_NUMBER: Parser<List<Char>> =
        rep(charIn('0'..'9'))

private val STRING_INTEGER: Parser<List<Char>> =
        (opt(charIn("-+")).map { it ?: '+' } then STRING_NUMBER).map { (s, n) -> (listOf(s) + n) }

val INTEGER: Parser<Int> =
        STRING_INTEGER.map { it.charsToInt() }

val FLOAT: Parser<Float> =
        (STRING_INTEGER then (opt((char('.') then STRING_NUMBER).map { (s, n) -> (listOf(s) + n) }).map {
            it ?: listOf()
        })).map { (s, n) -> (s + n).charsToFloat() }
