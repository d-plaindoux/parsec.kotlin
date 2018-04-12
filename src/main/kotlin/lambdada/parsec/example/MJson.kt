package lambdada.parsec.example

import lambdada.parsec.parser.*


// mjson ::= integer | ('[' (mjson (, mjson)*)? ']'

interface MJSon

data class MJSonInt(val v: Int) : MJSon
data class MJSonArray(val v: List<MJSon>) : MJSon

fun mjson(): Parser<MJSon> {
    val json: Parser<MJSon> = lazy { mjson() }
    val jsonInt: Parser<MJSon> = integer map { MJSonInt(it) }
    val jsonArray: Parser<MJSon> =
            char('[') then
            opt(json then optRep(char(',') thenRight json) map { (s, l) -> listOf(s) + l }) then
            char(']') map { MJSonArray(it.first.second.orElse(listOf())) }

    return jsonInt or jsonArray
}
