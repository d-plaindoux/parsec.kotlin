package lambdada.parsec.io

import lambdada.parsec.io.ReaderFromParser.Companion.tokenizeNow
import lambdada.parsec.parser.Parser


private class ReaderFromParser<I, A>(private val parser: Parser<I, A>, private val reader: Reader<I>) : Reader<A> {

    override fun location() = reader.location()
    override fun read() = parser(reader).fold({ it.value to tokenizeNow(parser, it.input) }, { null })

    companion object {
        internal fun <I, A> tokenizeNow(parser: Parser<I, A>, reader: Reader<I>): ReaderFromParser<I, A> =
                ReaderFromParser(parser, reader)
    }

}

infix fun <I, A> Reader<I>.tokenize(parser: Parser<I, A>): Reader<A> = tokenizeNow(parser, this)
