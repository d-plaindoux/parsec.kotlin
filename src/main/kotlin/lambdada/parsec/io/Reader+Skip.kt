package lambdada.parsec.io

import lambdada.parsec.parser.Parser
import lambdada.parsec.parser.Response.Accept
import lambdada.parsec.parser.Response.Reject

private class `Reader with Skip`(private val skip: Parser<*>, private val reader: Reader) : Reader(reader.position) {

    override fun next(): Pair<Char?, Reader> {
        val (c, input) = performSkip().next()
        return c to `Reader with Skip`(skip, input)
    }

    private fun performSkip(): Reader {
        val v = skip(reader)
        return when (v) {
            is Accept -> v.input
            is Reject -> reader
        }
    }

}

infix fun Reader.skip(r: Parser<*>): Reader = `Reader with Skip`(r, this)
