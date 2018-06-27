package lambdada.parsec.parser

import lambdada.parsec.io.CharReader
import org.junit.Assert.assertEquals
import org.junit.Test

class T06_IntegerParser {

    @Test
    fun shouldPositiveIntegerParserReturnAccept() {
        val parser = INTEGER then eos map { it.first }

        val result = parser.parse(CharReader.string("+42")).fold({ it.value == 42 }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldNegativeIntegerParserReturnAccept() {
        val parser = INTEGER then eos map { it.first }

        val result = parser.parse(CharReader.string("-42")).fold({ it.value == -42 }, { false })

        assertEquals(result, true)
    }

}