package lambdada.parsec.json

import lambdada.parsec.io.Reader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*


object JSonParserND : CharParser<JSon> {

    override fun invoke(r: Reader<Char>): Response<Char, JSon> = (json thenLeft eos)(removeSpaces(r))

    private fun removeSpaces(r: Reader<Char>): Reader<Char> = r skip rep(charIn("\r\n\t "))

    private val jsonNull: CharParser<JSon> =
            string("null") map { JSonNull }

    private val jsonBoolean: CharParser<JSon> =
            (string("true") map { JSonBoolean(true) as JSon }) or (string("false") map { JSonBoolean(false) as JSon })

    private val jsonInt: CharParser<JSon> =
            float map { JSonNumber(it) }

    private val jsonString: CharParser<JSon> =
            delimitedString() map { JSonString(it) as JSon }

    private fun <A> structure(p: CharParser<A>, o: Char, s: Char, c: Char): CharParser<List<A>?> =
            char(o) thenRight opt(p then optRep(char(s) thenRight p) map { (s, l) -> listOf(s) + l }) thenLeft char(c)

    private val jsonArray: CharParser<JSon> =
            structure(lazy { json }, '[', ',', ']') map { JSonArray(it.orEmpty()) }

    private val jsonAttribute: CharParser<Pair<String, JSon>> =
            delimitedString() thenLeft char(':') then lazy { json }

    private val jsonObject: CharParser<JSon> =
            structure(jsonAttribute, '{', ',', '}') map { JSonObject(it.orEmpty().toMap()) }

    private val json: CharParser<JSon> =
            jsonNull or jsonBoolean or jsonInt or jsonString or jsonArray or jsonObject

}
