package lambdada.parsec.example

import lambdada.parsec.parser.*


// JSon ::= integer | ('[' (JSon (, JSon)*)? ']'

interface JSon

data class JSonBoolean(val v: Boolean) : JSon
data class JSonNumber(val v: Float) : JSon
data class JSonString(val v: String) : JSon
data class JSonArray(val v: List<JSon>) : JSon
data class JSonObject(val v: Map<String, JSon>) : JSon
object JSonNull : JSon

fun json(): Parser<JSon> {
    fun <A> spaces(p: Parser<A>): Parser<A> = optRep(charIn("\n\r\t ")) thenRight p thenLeft optRep(charIn("\n\r\t "))

    val json: Parser<JSon> =
            lazy { json() }

    val jsonNull: Parser<JSon> =
            string("null") map { JSonNull }

    val jsonBoolean: Parser<JSon> =
            (string("true") map { JSonBoolean(true) as JSon }) or (string("false") map { JSonBoolean(false) as JSon })

    val jsonInt: Parser<JSon> =
            float map { JSonNumber(it) }

    val jsonString: Parser<JSon> =
            delimitedString() map { JSonString(it) as JSon }

    fun <A> structure(p: Parser<A>, o: Char, s: Char, c: Char): Parser<List<A>?> =
            char(o) thenRight opt(p then optRep(spaces(char(s)) thenRight p) map { (s, l) -> listOf(s) + l }) thenLeft spaces(char(c))

    val jsonArray: Parser<JSon> =
            structure(json, '[', ',', ']') map { JSonArray(it.orEmpty()) }

    val jsonObject: Parser<JSon> =
            structure(spaces(delimitedString()) thenLeft spaces(char(':')) then json, '{', ',', '}') map { JSonObject(it.orEmpty().toMap()) }

    return spaces(jsonNull or jsonBoolean or jsonInt or jsonString or jsonArray or jsonObject)
}
