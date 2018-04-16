package lambdada.parsec.example

import lambdada.parsec.io.Reader
import lambdada.parsec.io.Readers
import lambdada.parsec.parser.*

interface JSon

object JSonNull : JSon
data class JSonBoolean(val v: Boolean) : JSon
data class JSonNumber(val v: Float) : JSon
data class JSonString(val v: String) : JSon
data class JSonArray(val v: List<JSon>) : JSon
data class JSonObject(val v: Map<String, JSon>) : JSon

object JSonParser : Parser<JSon> {

    override fun invoke(r: Reader): Response<JSon> = (json thenLeft eos)(removeSpace(r))

    private fun removeSpace(r: Reader): Reader = Readers.skip(r, optRep(charIn("\r\n\t ")) map { Unit })

    private val jsonNull: Parser<JSon> =
            string("null") map { JSonNull }

    private val jsonBoolean: Parser<JSon> =
            (string("true") map { JSonBoolean(true) as JSon }) or (string("false") map { JSonBoolean(false) as JSon })

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

    private val json: Parser<JSon> = jsonNull or jsonBoolean or jsonInt or jsonString or jsonArray or jsonObject

}