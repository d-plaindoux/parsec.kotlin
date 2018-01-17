package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class FlowParserTest {

    @Test
    fun shouldSequenceParserReturnsAccept() {
        val parser = returns('a') then returns('b')

        Assert.assertEquals(parser.parse(Reader.new("")).fold({ it.value == Pair('a','b') }, { false }), true)
    }

    @Test
    fun shouldSequenceParserReturnsReject() {
        val parser = returns('a') then fails<Unit>()

        Assert.assertEquals(parser.parse(Reader.new("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldChoiceParserReturnsAccept() {
        val parser = returns('a') or returns('b')

        Assert.assertEquals(parser.parse(Reader.new("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldChoiceWithFailsParserReturnsAccept() {
        val parser = fails<Char>() or returns('b')

        Assert.assertEquals(parser.parse(Reader.new("")).fold({ it.value == 'b' }, { false }), true)
    }

}