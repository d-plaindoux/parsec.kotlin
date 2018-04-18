package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert.assertEquals
import org.junit.Test

class T04_CharParser {

    @Test
    fun shouldCharParserReturnsAccept() {
        val parser = char('a')

        assertEquals(parser(Readers.fromString("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldCharParserReturnsFails() {
        val parser = char('a')

        assertEquals(parser(Readers.fromString("b")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldCharInRangeParserReturnsAccept() {
        val parser = charIn('a'..'z')

        assertEquals(parser(Readers.fromString("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldCharInRangeParserReturnsFails() {
        val parser = charIn('b'..'z')

        assertEquals(parser(Readers.fromString("a")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldCharInStringParserReturnsAccept() {
        val parser = charIn("-+")

        assertEquals(parser(Readers.fromString("-")).fold({ it.value == '-' }, { false }), true)
    }

    @Test
    fun shouldCharInStringParserReturnsFails() {
        val parser = charIn("-+")

        assertEquals(parser(Readers.fromString("/")).fold({ false }, { true }), true)
    }


    @Test
    fun shouldCharParserOrParserReturnsAccept() {
        val parser = char('a') or char('b')

        assertEquals(parser(Readers.fromString("b")).fold({ it.value == 'b' }, { false }), true)
    }

}