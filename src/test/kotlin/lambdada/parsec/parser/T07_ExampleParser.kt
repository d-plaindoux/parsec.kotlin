package lambdada.parsec.parser

import lambdada.parsec.example.MJSonArray
import lambdada.parsec.example.MJSonInt
import lambdada.parsec.example.mjson
import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_ExampleParser {

    @Test
    fun shouldMJsonParserReturnInteger() {
        val parser = mjson()

        assertEquals(parser(Reader.new("42")).fold({ it.value == MJSonInt(42) }, { false }), true)
    }

    @Test
    fun shouldMJsonParserReturnEmptyArray() {
        val parser = mjson()

        assertEquals(parser(Reader.new("[]")).fold({ it.value == MJSonArray(listOf()) }, { false }), true)
    }

    @Test
    fun shouldMJsonParserReturnSingletonArray() {
        val parser = mjson()

        assertEquals(parser(Reader.new("[42]")).fold({ it.value == MJSonArray(listOf(MJSonInt(42))) }, { false }), true)
    }

    @Test
    fun shouldMJsonParserReturnArray() {
        val parser = mjson()

        assertEquals(parser(Reader.new("[42,43]")).fold({ it.value == MJSonArray(listOf(MJSonInt(42),MJSonInt(43))) }, { false }), true)
    }

}