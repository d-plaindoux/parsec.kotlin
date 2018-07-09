package lambdada.parsec.examples.expression

import lambdada.parsec.io.Reader
import lambdada.parsec.io.skip
import lambdada.parsec.parser.*

sealed class Operator
object Plus : Operator()
object Mult : Operator()

object ExpressionParser {

    private fun operation(c: Operator): (Float, Float) -> Float =
            when (c) {
                is Plus -> { p1, p2 -> p1 + p2 }
                is Mult -> { p1, p2 -> p1 * p2 }
            }

    fun SEXPR(): Parser<Char, Float> =
            FLOAT or (char('(') thenRight lazy { EXPR() } thenLeft char(')'))

    val OP = (char('+') map { Plus as Operator }) or (char('*') map { Mult as Operator })

    fun EXPR(): Parser<Char, Float> =
            lazy { SEXPR() then (OP then EXPR()).opt } map { p ->
                p.second?.let { operation(it.first)(p.first, it.second) } ?: p.first
            }

    fun reader(r: Reader<Char>): Reader<Char> = r skip charIn("\r\n\t ").rep

}

