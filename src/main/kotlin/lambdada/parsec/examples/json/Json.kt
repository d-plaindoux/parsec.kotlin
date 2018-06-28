package lambdada.parsec.examples.json

import lambdada.parsec.io.CharReader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*


object JSonParser {

    private val JSON_NULL: Parser<JSon> =
            string("null").map { JSonNull }

    private val JSON_TRUE: Parser<JSon> =
            string("true").map { JSonBoolean(true) }

    private val JSON_FALSE: Parser<JSon> =
            string("false").map { JSonBoolean(false) }

    private val JSON_INT: Parser<JSon> =
            FLOAT.map { JSonNumber(it) }

    private val JSON_STRING: Parser<JSon> =
            delimitedString().map { JSonString(it) }

    private fun <A> structure(p: Parser<A>, o: Char, s: Char, c: Char): Parser<List<A>?> =
            char(o) thenRight opt((p then optRep(char(s) thenRight p) thenLeft char(c)).map { (s, l) -> listOf(s) + l })

    private val JSON_ARRAY: Parser<JSon> =
            structure(lazy { JSON }, '[', ',', ']').map { JSonArray(it.orEmpty()) }

    private val JSON_ATTRIBUTE: Parser<Pair<String, JSon>> =
            delimitedString() thenLeft char(':') then lazy { JSON }

    private val JSON_OBJECT: Parser<JSon> =
            structure(JSON_ATTRIBUTE, '{', ',', '}').map { JSonObject(it.orEmpty().toMap()) }

    val JSON_ND: Parser<JSon> =
            JSON_NULL or
            JSON_TRUE or
            JSON_FALSE or
            JSON_INT or
            JSON_STRING or
            JSON_ARRAY or
            JSON_OBJECT

    val JSON: Parser<JSon> =
            lookahead(any).flatMap {
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

    fun reader(r: CharReader): CharReader = r skip rep(charIn("\r\n\t "))

}
