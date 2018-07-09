package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class T03_OccurrenceParser {

    @Test
    fun shouldOptionalParserWithEmptyStringReturnsAccept() {
        val parser = any<Char>().opt then eos()

        val result = parser.invoke(Reader.string("")).fold({ it.value.first == null }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldOptionalParserWithNonEmptyStringReturnsAccept() {
        val parser = any<Char>().opt then eos()

        val result = parser.invoke(Reader.string("a")).fold({ it.value.first == 'a' }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithEmptyStringReturnsAccept() {
        val parser = any<Char>().optRep then eos()

        val result = parser.invoke(Reader.string("")).fold({ true }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = any<Char>().optRep then eos()

        val result = parser.invoke(Reader.string("ab")).fold({ it.value.first == listOf('a', 'b') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableParserWithEmptyStringReturnsReject() {
        val parser = any<Char>().rep then eos()

        val result = parser.invoke(Reader.string("")).fold({ false }, { true })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = any<Char>().rep then eos()

        val result = parser.invoke(Reader.string("ab")).fold({ it.value.first == listOf('a', 'b') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableNotParserWithNonEmptyStringReturnsAccept() {
        val parser = not(char('a')).rep then eos()

        val result = parser.invoke(Reader.string("bc")).fold({ it.value.first == listOf('b', 'c') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableNotThenCharParserWithNonEmptyStringReturnsAccept() {
        val parser = not(char('a')).optRep

        val result = parser.invoke(Reader.string("bca")).fold({ it.value == listOf('b', 'c') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldBeAbleToParseLargeInput() {
        val parser = any<Char>().optRep thenLeft eos()

        val size = 16 * 1024
        val content = "a".repeat(size)

        val result = parser.invoke(Reader.string(content)).fold({ it.value.size }, { 0 })

        assertEquals(result, size)
    }

}