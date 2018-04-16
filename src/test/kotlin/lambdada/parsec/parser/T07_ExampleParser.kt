package lambdada.parsec.parser

import lambdada.parsec.example.*
import lambdada.parsec.io.Reader
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_ExampleParser {

    @Test
    fun shouldJSonParserReturnInteger() {
        val parser = json()

        assertEquals(parser(Reader.new("42")).fold({ it.value == JSonNumber(42F) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnNull() {
        val parser = json()

        assertEquals(parser(Reader.new("null")).fold({ it.value == JSonNull }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnTrue() {
        val parser = json()

        assertEquals(parser(Reader.new("true")).fold({ it.value == JSonBoolean(true) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnFalse() {
        val parser = json()

        assertEquals(parser(Reader.new("false")).fold({ it.value == JSonBoolean(false) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnString() {
        val parser = json()

        assertEquals(parser(Reader.new("\"42\"")).fold({ it.value == JSonString("42") }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyArray() {
        val parser = json()

        assertEquals(parser(Reader.new("[]")).fold({ it.value == JSonArray(listOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyObject() {
        val parser = json()

        assertEquals(parser(Reader.new("{}")).fold({ it.value == JSonObject(mapOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonObject() {
        val parser = json()

        assertEquals(parser(Reader.new("{\"a\":42}")).fold({ it.value == JSonObject(mapOf("a" to JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonArray() {
        val parser = json()

        assertEquals(parser(Reader.new("[42]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnArray() {
        val parser = json()

        assertEquals(parser(Reader.new("[42,43]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F), JSonNumber(43F))) }, { false }), true)
    }

    @Test
    fun shouldParseJSon1k() {
        val parser = json() thenLeft eos
        val content = T05_ExampleParser::class.java.getResource("/1k.json").readText()

        val response = parser(Reader.new(content))

        print(response)

        assertEquals(response.fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon100k() {
        val parser = json() thenLeft eos
        val content = T05_ExampleParser::class.java.getResource("/100k.json").readText()
        val response = parser(Reader.new(content))

        print(response)

        assertEquals(response.fold({ true }, { false }), true)
    }
}