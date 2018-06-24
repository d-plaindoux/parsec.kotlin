package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T01_MonadParser {

    fun Response<*>.isSuccess() : Boolean = this.get()?.let { true } ?: false


    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser = returns('a').map { it == 'a' }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = returns('a').flatMap { returns(it + "b") }.map { it == "ab" }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns('a').flatMap { fails<Char>() }

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    private fun givenAReader() = Reader.string("")

}
