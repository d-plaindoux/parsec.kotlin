package lambdada.parsec.parser

import lambdada.parsec.extension.charsToFloat
import lambdada.parsec.extension.charsToInt
import lambdada.parsec.extension.stringsToString

//
// Specific Char parsers
//

fun char(c: Char): CharParser<Char> =
        doTry(any satisfy { c == it })

fun charIn(c: CharRange): CharParser<Char> =
        doTry(any satisfy { c.contains(it) })

fun charIn(s: String): CharParser<Char> =
        doTry(any satisfy { s.contains(it) })

//
// Negation
//

fun not(p: CharParser<Char>): CharParser<Char> = CharParser {
    val a = p.invoke(it)
    when (a) {
        is Reject -> any.invoke(it)
        is Accept -> fails<Char>().invoke(it)
    }
}

//
// Characters parser
//

fun string(s: String): CharParser<String> =
        s.fold(returns(Unit), { a, c -> a thenLeft char(c) }) map { s }

fun delimitedString(): CharParser<String> {
    val anyChar: CharParser<String> = doTry(string("\\\"")) or (not(char('"')) map { it.toString() })
    return char('"') thenRight optRep(anyChar) thenLeft char('"') map { it.stringsToString() }
}

//
// Number parser
//

private val stringNumber: CharParser<List<Char>> =
        rep(charIn('0'..'9'))

private val stringInteger: CharParser<List<Char>> =
        opt(charIn("-+")) map { it ?: '+' } then stringNumber map { (s, n) -> (listOf(s) + n) }

val integer: CharParser<Int> =
        stringInteger map { it.charsToInt() }

val float: CharParser<Float> =
        stringInteger then (opt(char('.') then stringNumber map { (s, n) -> (listOf(s) + n) }) map {
            it ?: listOf()
        }) map { (s, n) -> (s + n).charsToFloat() }
