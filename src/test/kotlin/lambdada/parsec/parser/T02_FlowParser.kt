package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T02_FlowParser {

    private fun <A> Response<*, A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldSequenceParserReturnsAccept() {
        val parser = returns<Char, Char>('a') then returns(1)

        val result = parser.invoke(givenAReader()).fold({ it.value == 'a' to 1 }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldSequenceParserReturnsReject() {
        val parser = any then fails<Char, Unit>()

        val result = parser.invoke(givenAReader()).fold({ false }, { true })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldSequenceParserReturnsAcceptWithLeftValue() {
        val parser = returns<Char, Char>('a') thenLeft returns(1)

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, 'a')
    }

    @Test
    fun shouldSequenceParserReturnsAcceptWithRightValue() {
        val parser = returns<Char, Char>('a') thenRight returns(1)

        val result = parser.invoke(givenAReader()).get()

        Assert.assertEquals(result, 1)
    }

    @Test
    fun shouldChoiceParserReturnsAccept() {
        val parser = returns<Char, Char>('a') or returns('b')

        val result = parser.invoke(givenAReader()).fold({ it.value == 'a' }, { false })

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldChoiceWithFailsParserReturnsAccept() {
        val parser = fails<Char, Char>() or returns('b')

        val result = parser.invoke(givenAReader()).fold({ it.value == 'b' }, { false })

        Assert.assertEquals(result, true)
    }

    private fun givenAReader() = Reader.string("an example")


}