package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class T06_StringParser {

    @Test
    fun shouldStringParserReturnAccept() {
        val parser = string("hello") then eos map { it.first }

        assertEquals(parser(Reader.new("hello")).fold({ it.value == "hello" }, { false }), true)
    }

}