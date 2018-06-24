package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T00_CoreParser {

    private fun Response<*>.isSuccess() : Boolean = this.get()?.let { true } ?: false // Warning A?? is isomorphic to A?

    @Test
    fun shouldReturnsParserReturnsAccept() {
        val parser = returns(true)

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

    private fun givenAReader() = Reader.string("")

}
