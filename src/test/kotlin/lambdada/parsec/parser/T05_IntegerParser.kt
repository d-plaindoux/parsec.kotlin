package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_IntegerParser {

    @Test
    fun shouldPositiveIntegerParserReturnAccept() {
        val parser = integer then eos() map { it.first }

        assertEquals(parser(Readers.fromString("+42")).fold({ it.value == 42 }, { false }), true)
    }

    @Test
    fun shouldNegativeIntegerParserReturnAccept() {
        val parser = integer then eos() map { it.first }

        assertEquals(parser(Readers.fromString("-42")).fold({ it.value == -42 }, { false }), true)
    }
    
}