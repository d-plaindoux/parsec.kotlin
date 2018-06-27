package lambdada.parsec.io

import lambdada.parsec.io.CharReaderWithSkip.Companion.skipNow
import lambdada.parsec.parser.Parser

private class CharReaderWithSkip(private val skip: Parser<*>, private val charReader: CharReader) : CharReader {

    override fun location() = charReader.location()
    override fun canRead() = charReader.canRead()
    override fun read() = charReader.read().let { it.first to skipNow(skip, it.second) }

    companion object {
        internal fun skipNow(skip: Parser<*>, charReader: CharReader): CharReaderWithSkip =
                CharReaderWithSkip(skip, skip.parse(charReader).fold({ it.input }, { charReader }))
    }

}

infix fun CharReader.skip(skip: Parser<*>): CharReader = skipNow(skip, this)
