package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert.assertEquals
import org.junit.Test

class T06_StringParser {

    @Test
    fun shouldStringParserReturnAccept() {
        val parser = string("hello") thenLeft eos

        assertEquals(parser(Readers.new("hello")).fold({ it.value == "hello" }, { false }), true)
    }

    @Test
    fun shouldDelimitedStringParserReturnEmptyString() {
        val parser = delimitedString() thenLeft eos

        assertEquals(parser(Readers.new(""""hel\"lo"""")).fold({ it.value }, { null }), """hel\"lo""")
    }


    @Test
    fun shouldStringWithMetaCharacterParserReturnAccept() {
        val parser = string("hel\\nlo") thenLeft eos

        assertEquals(parser(Readers.new("hel\\nlo")).fold({ it.value == "hel\\nlo" }, { false }), true)
    }
}