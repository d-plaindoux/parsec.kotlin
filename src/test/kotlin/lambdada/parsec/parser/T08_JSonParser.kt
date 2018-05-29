package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import lambdada.parsec.json.*
import org.junit.Assert.assertEquals
import org.junit.Test

class T08_JSonParser {

    @Test
    fun shouldJSonParserReturnInteger() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("42")).fold({ it.value == JSonNumber(42F) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnNull() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("null")).fold({ it.value == JSonNull }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnTrue() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("true")).fold({ it.value == JSonBoolean(true) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnFalse() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("false")).fold({ it.value == JSonBoolean(false) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnString() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("\"42\"")).fold({ it.value == JSonString("42") }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyArray() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("[]")).fold({ it.value == JSonArray(listOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyObject() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("{}")).fold({ it.value == JSonObject(mapOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonObject() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("{\"a\":42}")).fold({ it.value == JSonObject(mapOf("a" to JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonArray() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("[42]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnArray() {
        val parser = JSonParser.json

        assertEquals(parser.invoke(Readers.string("[42,43]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F), JSonNumber(43F))) }, { false }), true)
    }

    @Test
    fun shouldParseJSon1k() {
        val parser = JSonParser.json
        val content = T08_JSonParser::class.java.getResource("/1k.json").readText()
        val reader = JSonParser.reader(Readers.string(content))

        assertEquals(parser.invoke(reader).fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon100k() {
        val parser = JSonParser.json
        val content = T08_JSonParser::class.java.getResource("/100k.json").readText()
        val reader = JSonParser.reader(Readers.string(content))

        assertEquals(parser.invoke(reader).fold({ true }, { false }), true)
    }

    @Test
    fun shouldParseJSon300k() {
        val parser = JSonParser.json
        val content = T08_JSonParser::class.java.getResource("/300k.json").readText()
        val reader = JSonParser.reader(Readers.string(content))

        assertEquals(parser.invoke(reader).fold({ true }, { false }), true)
    }

}