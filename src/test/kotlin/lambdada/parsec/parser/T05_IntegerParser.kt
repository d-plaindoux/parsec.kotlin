package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_IntegerParser {

    @Test
    fun shouldPositiveIntegerParserReturnAccept() {
        val parser = integer then eos map { it.first }

        val result = parser.invoke(Readers.string("+42")).fold({ it.value == 42 }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldNegativeIntegerParserReturnAccept() {
        val parser = integer then eos map { it.first }

        val result = parser.invoke(Readers.string("-42")).fold({ it.value == -42 }, { false })

        assertEquals(result, true)
    }
    
}