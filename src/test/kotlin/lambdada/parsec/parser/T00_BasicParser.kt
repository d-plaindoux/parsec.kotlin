package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import org.junit.Assert
import org.junit.Test

class T00_BasicParser {

    @Test
    fun shouldReturnsParserReturnsAccept() {
        val parser: Parser<Char, Char> = returns('a')

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldFailsParserReturnsError() {
        val parser = fails<Char, Char>()

        Assert.assertEquals(parser(Readers.fromString("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldMappedReturnsParserReturnsAccept() {
        val parser: Parser<Char, Boolean> = returns<Char, Char>('a') map { it == 'a' }

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value }, { false }), true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsAccept() {
        val parser = returns<Char, Char>('a') flatMap { returns<Char, String>(it + "b") } map { it == "ab" }

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value }, { false }), true)
    }

    @Test
    fun shouldFlapMappedReturnsParserReturnsError() {
        val parser = returns<Char, Char>('a') flatMap { fails<Char, Char>() }

        Assert.assertEquals(parser(Readers.fromString("")).fold({ false }, { true }), true)
    }

    @Test
    fun shouldLazyReturnsParserReturnsAccept() {
        val parser = lazy { returns<Char, Char>('a') }

        Assert.assertEquals(parser(Readers.fromString("")).fold({ it.value == 'a' }, { false }), true)
    }

    @Test
    fun shouldLazyFailsParserReturnsError() {
        val parser = lazy { fails<Char, Char>() }

        Assert.assertEquals(parser(Readers.fromString("")).fold({ false }, { true }), true)
    }

}
