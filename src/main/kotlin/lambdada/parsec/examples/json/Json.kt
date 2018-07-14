package lambdada.parsec.examples.json

import lambdada.parsec.io.Reader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*


object JSonParser {

    private val JSON_NULL: Parser<Char, JSon> =
            string("null") map { JSonNull }

    private val JSON_TRUE: Parser<Char, JSon> =
            string("true") map { JSonBoolean(true) }

    private val JSON_FALSE: Parser<Char, JSon> =
            string("false") map { JSonBoolean(false) }

    private val JSON_INT: Parser<Char, JSon> =
            FLOAT map { JSonNumber(it) }

    private val JSON_STRING: Parser<Char, JSon> =
            delimitedString() map { JSonString(it) }

    private fun <A> structure(p: Parser<Char, A>, o: Char, s: Char, c: Char): Parser<Char, List<A>?> =
            char(o) thenRight (p then (char(s) thenRight p).optrep thenLeft char(c) map { (s, l) -> listOf(s) + l }).opt

    private val JSON_ARRAY: Parser<Char, JSon> =
            structure(lazy { JSON }, '[', ',', ']') map { JSonArray(it.orEmpty()) }

    private val JSON_ATTRIBUTE: Parser<Char, Pair<String, JSon>> =
            delimitedString() thenLeft char(':') then lazy { JSON }

    private val JSON_OBJECT: Parser<Char, JSon> =
            structure(JSON_ATTRIBUTE, '{', ',', '}') map { JSonObject(it.orEmpty().toMap()) }

    val JSON: Parser<Char, JSon> =
            lookahead(Char.any) flatMap {
                when (it) {
                    'n' -> JSON_NULL
                    't' -> JSON_TRUE
                    'f' -> JSON_FALSE
                    '"' -> JSON_STRING
                    '[' -> JSON_ARRAY
                    '{' -> JSON_OBJECT
                    else -> JSON_INT
                }
            }

    fun reader(r: Reader<Char>): Reader<Char> = r skip charIn("\r\n\t ").rep

}
