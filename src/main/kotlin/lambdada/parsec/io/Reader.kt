package lambdada.parsec.io

import lambdada.parsec.parser.Accept
import lambdada.parsec.parser.Parser
import lambdada.parsec.parser.Reject

abstract class Reader<T>(open val offset: Int) {

    abstract fun next(): Pair<T?, Reader<T>>

}

private class `Reader from List`<T>(private val source: List<T>, override val offset: Int) : Reader<T>(offset) {

    override fun next(): Pair<T?, Reader<T>> {
        val v = source.getOrNull(offset)
        return when (v) {
            null -> null to this
            else -> v to `Reader from List`(source, offset + 1)
        }
    }
}

class Readers {
    companion object {
        fun string(s: String): Reader<Char> = `Reader from List`(s.toList(), 0)
    }

}

private class `Reader with Skip`<T>(private val skip: Parser<T, *>, private val reader: Reader<T>) : Reader<T>(reader.offset) {

    override fun next(): Pair<T?, Reader<T>> {
        val (c, input) = performSkip().next()
        return c to `Reader with Skip`(skip, input)
    }

    private fun performSkip(): Reader<T> {
        val v = skip(reader)
        return when (v) {
            is Accept -> v.input
            is Reject -> reader
        }
    }
}

infix fun <T> Reader<T>.skip(r: Parser<T, *>): Reader<T> = `Reader with Skip`(r, this)
