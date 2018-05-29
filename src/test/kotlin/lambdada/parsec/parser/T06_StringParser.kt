package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert.assertEquals
import org.junit.Test

class T06_StringParser {

    @Test
    fun shouldStringParserReturnAccept() {
        val parser = string("hello") thenLeft eos

        val result = parser.invoke(Readers.string("hello")).fold({ it.value == "hello" }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldDelimitedStringParserReturnEmptyString() {
        val parser = delimitedString() thenLeft eos

        val result = parser.invoke(Readers.string(""""hel\"lo"""")).fold({ it.value }, { null })

        assertEquals(result, """hel\"lo""")
    }

    @Test
    fun shouldStringWithMetaCharacterParserReturnAccept() {
        val parser = string("hel\\nlo") thenLeft eos

        val result = parser.invoke(Readers.string("hel\\nlo")).fold({ it.value == "hel\\nlo" }, { false })

        assertEquals(result, true)
    }
}