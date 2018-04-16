package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert
import org.junit.Test

class T01_FlowParser {

    @Test
    fun shouldSequenceParserReturnsAccept() {
        val parser = returns('a') then returns(1)

        Assert.assertEquals(parser(Readers.new("")).fold({ it.value == 'a' to 1 }, { false }), true)
    }

    @Test
    fun shouldSequenceParserReturnsReject() {
        val parser = returns('a') then fails<Unit>()

        Assert.assertEquals(parser(Readers.new("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldSequenceParserReturnsAcceptWithLeftValue() {
        val parser = returns('a') thenLeft returns(1)

        Assert.assertEquals(parser(Readers.new("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldSequenceParserReturnsAcceptWithRightValue() {
        val parser = returns('a') thenRight returns(1)

        Assert.assertEquals(parser(Readers.new("")).fold({ it.value == 1 }, { false }), true)
    }

    @Test
    fun shouldChoiceParserReturnsAccept() {
        val parser = returns('a') or returns('b')

        Assert.assertEquals(parser(Readers.new("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldChoiceWithFailsParserReturnsAccept() {
        val parser = fails<Char>() or returns('b')

        Assert.assertEquals(parser(Readers.new("")).fold({ it.value == 'b' }, { false }), true)
    }

}