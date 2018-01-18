package lambdada.parsec.example

import lambdada.parsec.parser.*


// mjson ::= integer | ('[' (mjson (, mjson)*)? ']'

interface MJSon

data class MJSonInt(val v: Int) : MJSon
data class MJSonArray(val v: List<MJSon>) : MJSon

fun mjson(): Parser<MJSon> {
    val jsonInt: Parser<MJSon> = integer map { MJSonInt(it) }
    val jsonArray: Parser<MJSon> =
            char('[') then
            opt(lazy { mjson() } then optRep(char(',') then lazy { mjson() } map { it.second }) map { (s, l) -> listOf(s) + l }) then  // (mjson (, mjson)*)? -> List<MJSon>
            char(']') map { MJSonArray(it.first.second.orElse(listOf())) }

    return jsonInt or jsonArray
}
