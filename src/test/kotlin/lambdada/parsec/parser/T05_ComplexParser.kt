package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_ComplexParser {

    @Test
    fun shouldCharParserReturnsAccept() {
        val parser = char('a')

        assertEquals(parser(Reader.new("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldCharParserReturnsFails() {
        val parser = char('a')

        assertEquals(parser(Reader.new("b")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldCharInRangeParserReturnsAccept() {
        val parser = charIn('a'..'z')

        assertEquals(parser(Reader.new("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldCharInRangeParserReturnsFails() {
        val parser = charIn('b'..'z')

        assertEquals(parser(Reader.new("a")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldCharInStringParserReturnsAccept() {
        val parser = charIn("-+")

        assertEquals(parser(Reader.new("-")).fold({ it.value == '-' }, { false }), true)
    }

    @Test
    fun shouldCharInStringParserReturnsFails() {
        val parser = charIn("-+")

        assertEquals(parser(Reader.new("/")).fold({ false }, { true }), true)
    }
}