package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class CharParserTest {

    @Test
    fun shouldCharParserReturnsAccept() {
        val parser = char('a')

        assertEquals(parser.parse(Reader.new("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldCharParserReturnsFails() {
        val parser = char('a')

        assertEquals(parser.parse(Reader.new("b")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldCharInRangeParserReturnsAccept() {
        val parser = charIn('a'..'z')

        assertEquals(parser.parse(Reader.new("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldCharInRangeParserReturnsFails() {
        val parser = charIn('b'..'z')

        assertEquals(parser.parse(Reader.new("a")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldCharInStringParserReturnsAccept() {
        val parser = charIn("-+")

        assertEquals(parser.parse(Reader.new("-")).fold({ it.value == '-' }, { false }), true)
    }

    @Test
    fun shouldCharInStringParserReturnsFails() {
        val parser = charIn("-+")

        assertEquals(parser.parse(Reader.new("/")).fold({ false }, { true }), true)
    }
}