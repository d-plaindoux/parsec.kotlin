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
            char('[') then
            opt(json then optRep(char(',') thenRight json) map { (s, l) -> listOf(s) + l }) then
            char(']') map { MJSonArray(it.first.second.orEmpty()) }

    val jsonAttribute : Parser<Pair<String,MJSon>> =
            delimitedString() thenLeft char(':') then json map { it }

    val jsonObject: Parser<MJSon> =
            char('{') then
            opt(jsonAttribute then optRep(char(',') thenRight jsonAttribute) map { (s, l) -> listOf(s) + l }) then
            char('}') map { MJSonObject(it.first.second.orEmpty().toMap()) }

    return jsonInt or jsonString or jsonArray or jsonObject

}
