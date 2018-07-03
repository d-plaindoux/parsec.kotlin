package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T01_MonadParser {

    private fun <A> Response<*, A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser = returns<Char, Char>('a').map { it -> it == 'a' }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldJoinReturnedReturns() {
        val parser = join(returns(any<Char>()))

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, 'a')
    }

    @Test
    fun shouldJoinReturnedReturnsWithConsumed() {
        val parser = join(returns(any<Char>()))

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldJoinReturnedReturnsWithoutConsumed() {
        val parser = join(returns(returns<Char, Char>('a')))

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, false)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = any<Char>().flatMap { returns<Char, String>(it + "b") }.map { it == "ab" }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsConsumed() {
        val parser = any<Char>().flatMap { returns<Char, String>(it + "b") }.map { it == "ab" }

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns<Char, Char>('a').flatMap { fails<Char, Char>() }

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldApplicativeReturnsAccept() {
        val parser = any<Char>() applicative returns { it: Char -> it == 'a' }

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldApplicativeReturnsAcceptAndConsume() {
        val parser = any<Char>() applicative returns { it: Char -> it == 'a' }

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldApplicativeReturnsReject() {
        val parser = any<Char>() applicative fails<Char, (Char) -> Boolean>()

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldApplicativeReturnsRejectAndConsume() {
        val parser = any<Char>() applicative fails<Char, (Char) -> Boolean>()

        val result = parser.invoke(givenAReader())

        Assert.assertEquals(result.consumed, true)
    }

    @Test
    fun shouldThenReturnsAccept() {
        val parser = { a: Char -> any<Char>() satisfy { it == a } }

        val result = (parser then parser)('a')(Reader.string("aa")).isSuccess()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldThenReturnsReject() {
        val parser = { a: Char -> any<Char>() satisfy { it == a } }

        val result = (parser then parser)('a')(Reader.string("ab")).isSuccess()

        Assert.assertEquals(result, false)
    }

    private fun givenAReader() = Reader.string("an example")

}
