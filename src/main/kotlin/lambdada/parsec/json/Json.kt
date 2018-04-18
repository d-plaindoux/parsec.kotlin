package lambdada.parsec.json

import lambdada.parsec.io.Reader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*


object JSonParser_1 : Parser<Char, JSon> {

    override fun invoke(r: Reader<Char>): Response<Char, JSon> = (json thenLeft eos())(removeSpace(r))

    private fun removeSpace(r: Reader<Char>): Reader<Char> = r skip rep(charIn("\r\n\t "))
    val jsonNull: Parser<Char, JSon> =
            string("null") map { JSonNull }

    private val jsonBoolean: Parser<Char, JSon> =
            (string("true") map { JSonBoolean(true) as JSon }) or (string("false") map { JSonBoolean(false) as JSon })

    private val jsonInt: Parser<Char, JSon> =
            float map { JSonNumber(it) }

    private val jsonString: Parser<Char, JSon> =
            delimitedString() map { JSonString(it) as JSon }

    private fun <A> structure(p: Parser<Char, A>, o: Char, s: Char, c: Char): Parser<Char, List<A>?> =
            char(o) thenRight opt(p then optRep(char(s) thenRight p) map { (s, l) -> listOf(s) + l }) thenLeft char(c)

    private val jsonArray: Parser<Char, JSon> =
            structure(lazy { json }, '[', ',', ']') map { JSonArray(it.orEmpty()) }

    private val jsonAttribute: Parser<Char, Pair<String, JSon>> =
            delimitedString() thenLeft char(':') then lazy { json }

    private val jsonObject: Parser<Char, JSon> =
            structure(jsonAttribute, '{', ',', '}') map { JSonObject(it.orEmpty().toMap()) }

    private val json: Parser<Char, JSon> = jsonNull or jsonBoolean or jsonInt or jsonString or jsonArray or jsonObject

}
