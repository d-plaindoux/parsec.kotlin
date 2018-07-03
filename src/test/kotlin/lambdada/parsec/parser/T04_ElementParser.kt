package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T04_ElementParser {

    @Test
    fun shouldAnyParserReturnsAccept() {
        val parser = any

        val result = parser.invoke(Reader.string("a")).fold({ it.value == 'a' && it.consumed }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldAnyParserReturnsReject() {
        val parser = any

        val result = parser.invoke(Reader.string("")).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldEOSParserReturnsAccept() {
        val parser = eos<Char>()

        val result = parser.invoke(Reader.string("")).fold({ true }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldEOSParserReturnsReject() {
        val parser = eos<Char>()

        val result = parser.invoke(Reader.string("a")).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldChoiceParserReturnsReject() {
        val parser = ((any thenLeft any) or any) then eos()

        val result = parser.invoke(Reader.string("a")).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldChoiceWithBacktrackParserReturnsAccept() {
        val parser = (doTry((any then any).map { it.first }) or any) then eos()

        val result = parser.invoke(Reader.string("a")).fold({ true }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldSatisfyParserReturnsAccept() {
        val parser = any

        val result = parser.invoke(Reader.string("a")).fold({ it.value == 'a' && it.consumed }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldNotSatisfyOrAnyParserReturnsAccept() {
        val parser = doTry(any.satisfy { it == 'a' }) or any

        val result = parser.invoke(Reader.string("b")).fold({ it.value == 'b' && it.consumed }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldNotCharParserReturnsAccept() {
        val parser = not(char('a'))

        val result = parser.invoke(Reader.string("b")).fold({ it.value == 'b' && it.consumed }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldNotCharParserReturnsReject() {
        val parser = not(char('a'))

        val result = parser.invoke(Reader.string("a")).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }
}