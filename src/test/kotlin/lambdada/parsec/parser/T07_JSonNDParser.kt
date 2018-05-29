package lambdada.parsec.parser

import lambdada.parsec.io.Readers
import lambdada.parsec.json.*
import org.junit.Assert.assertEquals
import org.junit.Test

class T07_JSonNDParser {

    @Test
    fun shouldJSonParserReturnInteger() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("42")).fold({ it.value == JSonNumber(42F) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserReturnNull() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("null")).fold({ it.value == JSonNull }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnTrue() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("true")).fold({ it.value == JSonBoolean(true) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnFalse() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("false")).fold({ it.value == JSonBoolean(false) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnString() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("\"42\"")).fold({ it.value == JSonString("42") }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnEmptyArray() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("[]")).fold({ it.value == JSonArray(listOf()) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnEmptyObject() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("{}"))
                .fold({ it.value == JSonObject(mapOf()) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnSingletonObject() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("{\"a\":42}"))
                .fold({ it.value == JSonObject(mapOf("a" to JSonNumber(42F))) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldJSonParserNDReturnSingletonArray() {
        val parser = JSonParser.jsonND

        assertEquals(parser.invoke(Readers.string("[42]"))
                .fold({ it.value == JSonArray(listOf(JSonNumber(42F))) }, { false }), true)
    }

    @Test
    fun shouldJSonParserNDReturnArray() {
        val parser = JSonParser.jsonND

        val result = parser.invoke(Readers.string("[42,43]"))
                .fold({ it.value == JSonArray(listOf(JSonNumber(42F), JSonNumber(43F))) }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldParseJSon1k() {
        val parser = JSonParser.jsonND
        val content = T07_JSonNDParser::class.java.getResource("/1k.json").readText()
        val reader = JSonParser.reader(Readers.string(content))

        val result = parser.invoke(reader).fold({ true }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldParseJSon100k() {
        val parser = JSonParser.jsonND
        val content = T07_JSonNDParser::class.java.getResource("/100k.json").readText()
        val reader = JSonParser.reader(Readers.string(content))

        val result = parser.invoke(reader).fold({ true }, { false })

        assertEquals(result, true)
    }

    @Test
    fun shouldParseJSon300k() {
        val parser = JSonParser.jsonND
        val content = T07_JSonNDParser::class.java.getResource("/300k.json").readText()
        val reader = JSonParser.reader(Readers.string(content))

        val result = parser.invoke(reader).fold({ true }, { false })

        assertEquals(result, true)
    }

}