package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt

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
        }) map { (s, n) ->
            (s + n).charsToFloat()
        }
