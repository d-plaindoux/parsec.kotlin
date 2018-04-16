package lambdada.parsec.parser

import lambdada.parsec.example.*
import lambdada.parsec.io.Readers
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_ExampleParser {

    @Test
    fun shouldJSonParserReturnInteger() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("42")).fold({ it.value == JSonNumber(42F) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnNull() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("null")).fold({ it.value == JSonNull }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnTrue() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("true")).fold({ it.value == JSonBoolean(true) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnFalse() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("false")).fold({ it.value == JSonBoolean(false) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnString() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("\"42\"")).fold({ it.value == JSonString("42") }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyArray() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("[]")).fold({ it.value == JSonArray(listOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyObject() {
        val parser = JSonParser

        print(parser(Readers.new("{}a")))

        assertEquals(parser(Readers.new("{}")).fold({ it.value == JSonObject(mapOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonObject() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("{\"a\":42}")).fold({ it.value == JSonObject(mapOf("a" to JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonArray() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("[42]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnArray() {
        val parser = JSonParser

        assertEquals(parser(Readers.new("[42,43]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F), JSonNumber(43F))) }, { false }), true)
    }

    @Test
    fun shouldParseJSon1k() {
        val parser = JSonParser
        val content = T05_ExampleParser::class.java.getResource("/1k.json").readText()

        assertEquals(parser(Readers.new(content)).fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon100k() {
        val parser = JSonParser
        val content = T05_ExampleParser::class.java.getResource("/100k.json").readText()
        val response = parser(Readers.new(content))

        print(response)

        assertEquals(response.fold({ true }, { false }), true)
    }
}