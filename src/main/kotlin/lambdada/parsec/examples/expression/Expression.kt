package lambdada.parsec.examples.expression

import lambdada.parsec.io.CharReader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*

object ExpressionParser {

    // Missing dependant types !
    private fun operation(c: Char): (Float, Float) -> Float = { p1, p2 ->
        when (c) {
            '*' -> p1 * p2
            else -> p1 + p2
        }
    }

    private fun SEXPR(): Parser<Float> =
            FLOAT or (char('(') thenRight lazy { EXPR() } thenLeft char(')'))

    fun EXPR(): Parser<Float> =
            lazy { SEXPR() then opt(charIn("+*") then EXPR()) } map { p ->
                p.second?.let { operation(it.first)(p.first, it.second) } ?: p.first
            }

    fun reader(r: CharReader): CharReader = r skip rep(charIn("\r\n\t "))

}

