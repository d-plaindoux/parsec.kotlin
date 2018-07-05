package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T00_CoreParser {

    private fun <A> Response<*, A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldReturnsParserReturnsAccept() {
        val parser: Parser<Char, Boolean> = returns(true)

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldFailsParserReturnsError() {
        val parser = fails<Char, Char>()

        val result = parser.invoke(givenAReader()).isSuccess()

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldLazyReturnsParserReturnsAccept() {
        val parser = lazy { returns<Char, Char>('a') }

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, 'a')
    }

    @Test
    fun shouldLazyFailsParserReturnsError() {
        val parser = lazy { fails<Char, Char>() }

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldAnyReturnsSuccess() {
        val parser = any<Char>()

        val result = parser.invoke(givenAReader("a")).fold({ true }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldAnyReturnsReject() {
        val parser = any<Char>()

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldNotReturnsReject() {
        val parser = not(any<Char>())

        val result = parser.invoke(givenAReader("a")).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    private fun givenAReader(s:String = "") = Reader.string(s)

}
