package lambdada.parsec.parser

import lambdada.parsec.io.CharReader
import org.junit.Assert
import org.junit.Test

class T01_MonadParser {

    private fun <A> Response<A>.get(): A? = this.fold({ it.value }, { null })
    private fun Response<*>.isSuccess(): Boolean = this.fold({ true }, { false })

    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser = returns('a').map { it == 'a' }

        val result = parser.parse(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldJoinReturnedReturns() {
        val parser = join(returns(any))

        val result = parser.parse(givenAReader()).get()

        Assert.assertEquals(result, 'a')
    }

    @Test
    fun shouldJoinReturnedReturnsWithConsumed() {
        val parser = join(returns(any))

        val result = parser.parse(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldJoinReturnedReturnsWithoutConsumed() {
        val parser = join(returns(returns('a')))

        val result = parser.parse(givenAReader())

        Assert.assertEquals(result.consumed, false)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = any.flatMap { returns(it + "b") }.map { it == "ab" }

        val result = parser.parse(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsConsumed() {
        val parser = any.flatMap { returns(it + "b") }.map { it == "ab" }

        val result = parser.parse(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns('a').flatMap { fails<Char>() }

        val result = parser.parse(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    private fun givenAReader() = CharReader.string("an example")

}
