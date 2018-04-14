package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class T03_OccurrenceParser {

    @Test
    fun shouldOptionalParserWithEmptyStringReturnsAccept() {
        val parser = opt(any) then eos

        assertEquals(parser(Reader.new("")).fold({ it.value.first == null }, { false }), true)
    }

    @Test
    fun shouldOptionalParserWithNonEmptyStringReturnsAccept() {
        val parser = opt(any) then eos

        assertEquals(parser(Reader.new("a")).fold({ it.value.first == 'a' }, { false }), true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithEmptyStringReturnsAccept() {
        val parser = optRep(any) then eos

        assertEquals(parser(Reader.new("")).fold({ true }, { false }), true)
    }

    @Test
    fun shouldOptionalRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = optRep(any) then eos

        assertEquals(parser(Reader.new("ab")).fold({ it.value.first == listOf('a','b') }, { false }), true)
    }

    @Test
    fun shouldRepeatableParserWithEmptyStringReturnsReject() {
        val parser = rep(any) then eos

        assertEquals(parser(Reader.new("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldRepeatableParserWithNonEmptyStringReturnsAccept() {
        val parser = rep(any) then eos

        assertEquals(parser(Reader.new("ab")).fold({ it.value.first == listOf('a','b') }, { false }), true)
    }

    @Test
    fun shouldRepeatableNotParserWithNonEmptyStringReturnsAccept() {
        val parser = rep(not(char('a'))) then eos

        assertEquals(parser(Reader.new("bc")).fold({ it.value.first == listOf('b','c') }, { false }), true)
    }

    @Test
    fun shouldRepeatableNotThenCharParserWithNonEmptyStringReturnsAccept() {
        val parser : Parser<List<Char>> = optRep(not(char('a')))

        assertEquals(parser(Reader.new("bca")).fold({ it.value == listOf('b','c') }, { false }), true)
    }
}