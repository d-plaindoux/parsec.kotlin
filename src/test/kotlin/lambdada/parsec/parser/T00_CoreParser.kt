package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert
import org.junit.Test

class T00_CoreParser {

    @Test
    fun shouldReturnsParserReturnsAccept() {
        val parser = returns(true)

        val result = parser.invoke(givenAReader()).fold({ it.value }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFailsParserReturnsError() {
        val parser = fails<Char>()

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser = returns('a') map { it == 'a' }

        val result = parser.invoke(givenAReader()).fold({ it.value }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = returns('a') flatMap { returns(it + "b") } map { it == "ab" }

        val result = parser.invoke(givenAReader()).fold({ it.value }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns('a') flatMap { fails<Char>() }

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldLazyReturnsParserReturnsAccept() {
        val parser = lazy { returns('a') } map { it == 'a' }

        val result = parser.invoke(givenAReader()).fold({ it.value }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldLazyFailsParserReturnsError() {
        val parser = lazy { fails<Char>() }

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    private fun givenAReader() = Readers.string("")

}
