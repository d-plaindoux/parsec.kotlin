package lambdada.parsec.parser

import lambdada.parsec.io.CharReader
import org.junit.Assert
import org.junit.Test

class T00_CoreParser {

    private fun <A> Response<A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldReturnsParserReturnsAccept() {
        val parser : Parser<Boolean> = returns(true)

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFailsParserReturnsError() {
        val parser = fails<Char>()

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldLazyReturnsParserReturnsAccept() {
        val parser = lazy { returns('a') }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, 'a')
    }

    @Test
    fun shouldLazyFailsParserReturnsError() {
        val parser = lazy { fails<Char>() }

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    private fun givenAReader() = CharReader.string("")

}
