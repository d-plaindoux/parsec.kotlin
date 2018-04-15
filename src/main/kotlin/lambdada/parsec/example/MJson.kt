package lambdada.parsec.example

import lambdada.parsec.parser.*


// mjson ::= integer | ('[' (mjson (, mjson)*)? ']'

interface MJSon

data class MJSonInt(val v: Int) : MJSon
data class MJSonString(val v: String) : MJSon
data class MJSonArray(val v: List<MJSon>) : MJSon
data class MJSonObject(val v: Map<String,MJSon>) : MJSon

fun mjson(): Parser<MJSon> {

    val json: Parser<MJSon> =
            lazy { mjson() }

    val jsonInt: Parser<MJSon> =
            integer map { MJSonInt(it) }

    val jsonString: Parser<MJSon> =
           delimitedString() map { MJSonString(it) }

    val jsonArray: Parser<MJSon> =
            char('[') thenRight
            opt(json then optRep(char(',') thenRight json) map { (s, l) -> listOf(s) + l }) thenLeft
            char(']') map { MJSonArray(it.orEmpty()) }

    val jsonAttribute : Parser<Pair<String,MJSon>> =
            delimitedString() thenLeft char(':') then json

    val jsonObject: Parser<MJSon> =
            char('{') thenRight
            opt(jsonAttribute then optRep(char(',') thenRight jsonAttribute) map { (s, l) -> listOf(s) + l }) thenLeft
            char('}') map { MJSonObject(it.orEmpty().toMap()) }

    return jsonInt or jsonString or jsonArray or jsonObject

}
