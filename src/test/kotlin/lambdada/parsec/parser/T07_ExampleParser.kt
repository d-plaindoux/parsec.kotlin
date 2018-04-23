package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import lambdada.parsec.json.*
import org.junit.Assert.assertEquals
import org.junit.Test

class T05_ExampleParser {

    @Test
    fun shouldJSonParserReturnInteger() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("42")).fold({ it.value == JSonNumber(42F) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnNull() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("null")).fold({ it.value == JSonNull }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnTrue() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("true")).fold({ it.value == JSonBoolean(true) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnFalse() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("false")).fold({ it.value == JSonBoolean(false) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnString() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("\"42\"")).fold({ it.value == JSonString("42") }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyArray() {
        val parser = JSonParser

        print(parser(Readers.fromString("[]")))

        assertEquals(parser(Readers.fromString("[]")).fold({ it.value == JSonArray(listOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyObject() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("{}")).fold({ it.value == JSonObject(mapOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonObject() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("{\"a\":42}")).fold({ it.value == JSonObject(mapOf("a" to JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonArray() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("[42]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnArray() {
        val parser = JSonParser

        assertEquals(parser(Readers.fromString("[42,43]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F), JSonNumber(43F))) }, { false }), true)
    }

    @Test
    fun shouldParseJSon1k() {
        val parser = JSonParser
        val content = T05_ExampleParser::class.java.getResource("/1k.json").readText()

        assertEquals(parser(Readers.fromString(content)).fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon100k() {
        val parser = JSonParser
        val content = T05_ExampleParser::class.java.getResource("/100k.json").readText()

        assertEquals(parser(Readers.fromString(content)).fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon300k() {
        val parser = JSonParser
        val content = T05_ExampleParser::class.java.getResource("/300k.json").readText()

        assertEquals(parser(Readers.fromString(content)).fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon1m() {
        val parser = JSonParser
        val content = T05_ExampleParser::class.java.getResource("/1m.json").readText()

        assertEquals(parser(Readers.fromString(content)).fold({ true }, { false }), true)
    }

}