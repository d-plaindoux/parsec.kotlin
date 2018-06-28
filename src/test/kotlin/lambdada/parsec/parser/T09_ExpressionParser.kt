package lambdada.parsec.parser

import lambdada.parsec.examples.expression.ExpressionParser
import lambdada.parsec.io.CharReader
import org.junit.Assert.assertEquals
import org.junit.Test

class T09_ExpressionParser {

    private fun <A> Response<A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldParseNumber() {
        val parser = ExpressionParser.EXPR() thenLeft eos

        assertEquals(parser.invoke(CharReader.string("42")).get(), 42f)
    }

    @Test
    fun shouldParseAddition() {
        val parser = ExpressionParser.EXPR() thenLeft eos

        assertEquals(parser.invoke(CharReader.string("42+23")).get(), 65f)
    }

    @Test
    fun shouldParseParenthesis() {
        val parser = ExpressionParser.EXPR() thenLeft eos

        assertEquals(parser.invoke(CharReader.string("(42)")).get(), 42f)
    }

    @Test
    fun shouldParseAdditionAndMultiplication() {
        val parser = ExpressionParser.EXPR() thenLeft eos

        assertEquals(parser.invoke(CharReader.string("42+(23*2)")).get(), 42f+(23f*2f))
    }
}