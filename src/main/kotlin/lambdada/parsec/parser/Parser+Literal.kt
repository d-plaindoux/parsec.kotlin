package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.stringsToString

//
// Specific Char parsers
//

fun char(c: Char): Parser<Char, Char> = doTry(any<Char>() satisfy { c == it })

fun charIn(c: CharRange): Parser<Char, Char> = doTry(any<Char>() satisfy { c.contains(it) })

fun charIn(s: String): Parser<Char, Char> = doTry(any<Char>() satisfy { it in s })

fun charIn(vararg s: Char): Parser<Char, Char> = doTry(any<Char>() satisfy { it in s })

//
// Characters parser
//

fun string(s: String): Parser<Char, String> = s.fold(returns<Char, Unit>(Unit)) { a, c -> a thenLeft char(c) } map { s }

fun delimitedString(): Parser<Char, String> = char('"') thenRight
        optRep(doTry(string("\\\"")) or (not(char('"')).map(Char::toString))) thenLeft
        char('"') map { it.stringsToString() }

//
// Number parser
//

private val STRING_NUMBER: Parser<Char, List<Char>> = rep(charIn('0'..'9'))

private val STRING_INTEGER: Parser<Char, List<Char>> =
        opt(charIn("-+")) map { it ?: '+' } then STRING_NUMBER map { listOf(it.first) + it.second }

val INTEGER: Parser<Char, Int> = STRING_INTEGER map { it.charsToInt() }

val FLOAT: Parser<Char, Float> =
        STRING_INTEGER then (opt(char('.') then STRING_NUMBER map { (s, n) -> (listOf(s) + n) }) map {
            it ?: listOf()
        }) map { (s, n) -> (s + n).charsToFloat() }
