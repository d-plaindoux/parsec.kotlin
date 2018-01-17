package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T02_ElementParser {

    @Test
    fun shouldAnyParserReturnsAccept() {
        val parser = any

        Assert.assertEquals(parser(Reader.new("a")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldAnyParserReturnsReject() {
        val parser = any

        Assert.assertEquals(parser(Reader.new("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldEOSParserReturnsAccept() {
        val parser = eos

        Assert.assertEquals(parser(Reader.new("")).fold({ true }, { false }), true)
    }

    @Test
    fun shouldEOSParserReturnsReject() {
        val parser = eos

        Assert.assertEquals(parser(Reader.new("a")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldChoiceParserReturnsReject() {
        val parser = ((any then any map { it.first }) or any) then eos

        Assert.assertEquals(parser(Reader.new("a")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldChoiceWithBacktrackParserReturnsAccept() {
        val parser = (doTry(any then any map { it.first }) or any) then eos

        Assert.assertEquals(parser(Reader.new("a")).fold({ true }, { false }), true)
    }

}