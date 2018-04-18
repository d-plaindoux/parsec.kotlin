package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert
import org.junit.Test

class T01_FlowParser {

    @Test
    fun shouldSequenceParserReturnsAccept() {
        val parser = returns<Char, Char>('a') then returns(1)

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 'a' to 1 }, { false }), true)
    }

    @Test
    fun shouldSequenceParserReturnsReject() {
        val parser = returns<Char, Char>('a') then fails<Char, Unit>()

        Assert.assertEquals(parser(Readers.fromString("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldSequenceParserReturnsAcceptWithLeftValue() {
        val parser = returns<Char, Char>('a') thenLeft returns(1)

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldSequenceParserReturnsAcceptWithRightValue() {
        val parser = returns<Char, Char>('a') thenRight returns(1)

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 1 }, { false }), true)
    }

    @Test
    fun shouldChoiceParserReturnsAccept() {
        val parser = returns<Char, Char>('a') or returns('b')

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldChoiceWithFailsParserReturnsAccept() {
        val parser = fails<Char, Char>() or returns('b')

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 'b' }, { false }), true)
    }

}