package lambdada.parsec.io

import lambdada.parsec.io.ReaderWithSkip.Companion.skipNow
import lambdada.parsec.parser.Parser

private class ReaderWithSkip<A>(private val skip: Parser<A, *>, private val charReader: Reader<A>) : Reader<A> {

    override fun location() = charReader.location()
    override fun canRead() = charReader.canRead()
    override fun read() = charReader.read().let { it.first to skipNow(skip, it.second) }

    companion object {
        internal fun <A> skipNow(skip: Parser<A, *>, charReader: Reader<A>): ReaderWithSkip<A> =
                ReaderWithSkip(skip, skip.invoke(charReader).fold({ it.input }, { charReader }))
    }

}

infix fun <A> Reader<A>.skip(skip: Parser<A, *>): Reader<A> = skipNow(skip, this)
