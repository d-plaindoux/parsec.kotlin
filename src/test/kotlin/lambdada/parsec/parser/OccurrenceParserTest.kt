package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class OccurrenceParserTest {

    @Test
    fun shouldOptionalParserWithEmptyStringReturnsAccept() {
        val parser = opt(any) then eos

        assertEquals(parser.parse(Reader.new("")).fold({ !it.value.first.isPresent }, { false }), true)
    }

    @Test
    fun shouldOptionalParserWithNonEmptyStringReturnsAccept() {
        val parser = opt(any) then eos

        assertEquals(parser.parse(Reader.new("a")).fold({ it.value.first.get() == 'a' }, { false }), true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithEmptyStringReturnsAccept() {
        val parser = optRep(any) then eos

        assertEquals(parser.parse(Reader.new("")).fold({ true }, { false }), true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = optRep(any) then eos

        assertEquals(parser.parse(Reader.new("ab")).fold({ it.value.first == listOf('a','b') }, { false }), true)
    }

    @Test
    fun shouldRepeatableParserWithEmptyStringReturnsReject() {
        val parser = rep(any) then eos

        assertEquals(parser.parse(Reader.new("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = rep(any) then eos

        assertEquals(parser.parse(Reader.new("ab")).fold({ it.value.first == listOf('a','b') }, { true }), true)
    }
}