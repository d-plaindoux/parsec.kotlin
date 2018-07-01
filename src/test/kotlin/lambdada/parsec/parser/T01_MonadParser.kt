package lambdada.parsec.parser

import lambdada.parsec.io.CharReader
import org.junit.Assert
import org.junit.Test

class T01_MonadParser {

    private fun <A> Response<A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser: Parser<Boolean> = returns('a').map { it -> it == 'a' }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldJoinReturnedReturns() {
        val parser = join(returns(any))

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, 'a')
    }

    @Test
    fun shouldJoinReturnedReturnsWithConsumed() {
        val parser = join(returns(any))

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldJoinReturnedReturnsWithoutConsumed() {
        val parser = join(returns(returns('a')))

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, false)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = any.flatMap { returns(it + "b") }.map { it == "ab" }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsConsumed() {
        val parser = any.flatMap { returns(it + "b") }.map { it == "ab" }

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns('a').flatMap { fails<Char>() }

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldApplicativeReturnsAccept() {
        val parser = any applicative returns { it: Char -> it == 'a' }

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldApplicativeReturnsAcceptAndConsume() {
        val parser = any applicative returns { it: Char -> it == 'a' }

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldApplicativeReturnsReject() {
        val parser = any applicative fails<(Char) -> Boolean>()

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldApplicativeReturnsRejectAndConsume() {
        val parser = any applicative fails<(Char) -> Boolean>()

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldThenReturnsAccept() {
        val parser = { a: Char -> any satisfy { it == a } }

        val result = (parser then parser)('a')(CharReader.string("aa")).isSuccess()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldThenReturnsReject() {
        val parser = { a: Char -> any satisfy { it == a } }

        val result = (parser then parser)('a')(CharReader.string("ab")).isSuccess()

        Assert.assertEquals(result, false)
    }

    private fun givenAReader() = CharReader.string("an example")

}
