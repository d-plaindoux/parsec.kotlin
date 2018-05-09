package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert
import org.junit.Test

class T00_BasicParser {

    @Test
    fun shouldReturnsParserReturnsAccept() {
        val parser: CharParser<Boolean> = returns(true)

        Assert.assertEquals(parser(givenAReader()).fold({ it.value }, { false }), true)
    }

    @Test
    fun shouldFailsParserReturnsError() {
        val parser = fails<Char>()

        Assert.assertEquals(parser(givenAReader()).fold({ false }, { true }), true)
    }

    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser: CharParser<Boolean> = returns('a') map { it == 'a' }

        Assert.assertEquals(parser(givenAReader()).fold({ it.value }, { false }), true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = returns('a') flatMap { returns(it + "b") } map { it == "ab" }

        Assert.assertEquals(parser(givenAReader()).fold({ it.value }, { false }), true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns('a') flatMap { fails<Char>() }

        Assert.assertEquals(parser(givenAReader()).fold({ false }, { true }), true)
    }

    @Test
    fun shouldLazyReturnsParserReturnsAccept() {
        val parser = lazy { returns('a') } map { it == 'a' }

        Assert.assertEquals(parser(givenAReader()).fold({ it.value }, { false }), true)
    }

    @Test
    fun shouldLazyFailsParserReturnsError() {
        val parser = lazy { fails<Char>() }

        Assert.assertEquals(parser(givenAReader()).fold({ false }, { true }), true)
    }

    private fun givenAReader() = Readers.fromString("")

}
