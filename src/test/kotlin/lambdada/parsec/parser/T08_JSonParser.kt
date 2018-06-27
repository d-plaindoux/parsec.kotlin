package lambdada.parsec.parser

import lambdada.parsec.examples.json.*
import lambdada.parsec.io.CharReader
import org.junit.Assert.assertEquals
import org.junit.Test

class T08_JSonParser {

    @Test
    fun shouldJSonParserReturnInteger() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("42")).fold({ it.value == JSonNumber(42F) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnNull() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("null")).fold({ it.value == JSonNull }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnTrue() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("true")).fold({ it.value == JSonBoolean(true) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnFalse() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("false")).fold({ it.value == JSonBoolean(false) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnString() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("\"42\"")).fold({ it.value == JSonString("42") }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyArray() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("[]")).fold({ it.value == JSonArray(listOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnEmptyObject() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("{}")).fold({ it.value == JSonObject(mapOf()) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonObject() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("{\"a\":42}")).fold({ it.value == JSonObject(mapOf("a" to JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnSingletonArray() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("[42]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserReturnArray() {
        val parser = JSonParser.JSON

        assertEquals(parser.parse(CharReader.string("[42,43]")).fold({ it.value == JSonArray(listOf(JSonNumber(42F), JSonNumber(43F))) }, { false }), true)
    }

    @Test
    fun shouldParseJSon1k() {
        val parser = JSonParser.JSON
        val url = T08_JSonParser::class.java.getResource("/1k.json")
        val reader = JSonParser.reader(CharReader.url(url))

        assertEquals(parser.parse(reader).fold({ true }, { false }), true)
    }

}