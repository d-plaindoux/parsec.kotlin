package lambdada.parsec.json

import lambdada.parsec.io.Reader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*

object JSonParser : Parser<JSon> {

    override fun invoke(r: Reader<Char>): Response<Char, JSon> = (json thenLeft eos)(removeSpace(r))

    private fun removeSpace(r: Reader<Char>): Reader<Char> = r skip rep(charIn("\r\n\t "))

    private val jsonNull: Parser<JSon> =
            string("null") map { JSonNull }

    private val jsonTrue: Parser<JSon> =
            string("true") map { JSonBoolean(true) as JSon }

    private val jsonFalse: Parser<JSon> =
            string("false") map { JSonBoolean(false) as JSon }

    private val jsonInt: Parser<JSon> =
            float map { JSonNumber(it) }

    private val jsonString: Parser<JSon> =
            delimitedString() map { JSonString(it) as JSon }

    private fun <A> structure(p: Parser<A>, o: Char, s: Char, c: Char): Parser<List<A>?> =
            char(o) thenRight opt(p then optRep(char(s) thenRight p) map { (s, l) -> listOf(s) + l }) thenLeft char(c)

    private val jsonArray: Parser<JSon> =
            structure(lazy { json }, '[', ',', ']') map { JSonArray(it.orEmpty()) }

    private val jsonAttribute: Parser<Pair<String, JSon>> =
            delimitedString() thenLeft char(':') then lazy { json }

    private val jsonObject: Parser<JSon> =
            structure(jsonAttribute, '{', ',', '}') map { JSonObject(it.orEmpty().toMap()) }

    private val json: Parser<JSon> =
            lookahead(any) flatMap {
                when (it) {
                    'n' -> jsonNull
                    't' -> jsonTrue
                    'f' -> jsonFalse
                    '"' -> jsonString
                    '[' -> jsonArray
                    '{' -> jsonObject
                    else -> jsonInt
                }
            }

}
