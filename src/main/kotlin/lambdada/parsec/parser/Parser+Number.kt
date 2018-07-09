package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt

//
// Number parser
//

private val STRING_NUMBER: Parser<Char, List<Char>> = charIn('0'..'9').rep

private val STRING_INTEGER: Parser<Char, List<Char>> =
        charIn("-+").opt map {
            it ?: '+'
        } then STRING_NUMBER map {
            listOf(it.first) + it.second
        }

val INTEGER: Parser<Char, Int> = STRING_INTEGER map { it.charsToInt() }

val FLOAT: Parser<Char, Float> =
        STRING_INTEGER then ((char('.') then STRING_NUMBER map {
            (listOf(it.first) + it.second)
        }).opt map {
            it ?: listOf()
        }) map {
            (it.first + it.second).charsToFloat()
        }
