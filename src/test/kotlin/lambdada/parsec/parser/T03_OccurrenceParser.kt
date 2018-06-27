package lambdada.parsec.parser

import lambdada.parsec.io.CharReader
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class T03_OccurrenceParser {

    @Test
    fun shouldOptionalParserWithEmptyStringReturnsAccept() {
        val parser = opt(any) then eos

        val result = parser.parse(CharReader.string("")).fold({ it.value.first == null }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldOptionalParserWithNonEmptyStringReturnsAccept() {
        val parser = opt(any) then eos

        val result = parser.parse(CharReader.string("a")).fold({ it.value.first == 'a' }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithEmptyStringReturnsAccept() {
        val parser = optRep(any) then eos

        val result = parser.parse(CharReader.string("")).fold({ true }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = optRep(any) then eos

        val result = parser.parse(CharReader.string("ab")).fold({ it.value.first == listOf('a', 'b') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableParserWithEmptyStringReturnsReject() {
        val parser = rep(any) then eos

        val result = parser.parse(CharReader.string("")).fold({ false }, { true })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = rep(any) then eos

        val result = parser.parse(CharReader.string("ab")).fold({ it.value.first == listOf('a', 'b') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableNotParserWithNonEmptyStringReturnsAccept() {
        val parser = rep(not(char('a'))) then eos

        val result = parser.parse(CharReader.string("bc")).fold({ it.value.first == listOf('b', 'c') }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldRepeatableNotThenCharParserWithNonEmptyStringReturnsAccept() {
        val parser = optRep(not(char('a')))

        val result = parser.parse(CharReader.string("bca")).fold({ it.value == listOf('b', 'c') }, { false })

        assertEquals(result, true)
    }

    @Ignore @Test
    fun shouldBeAbleToParseLargeInput() {
        val parser = optRep(any) thenLeft  eos

        val size = 1024
        val content = "a".repeat(size)

        val result = parser.parse(CharReader.string(content)).fold({ it.value.size }, { 0 })

        assertEquals(result, size)
    }

}