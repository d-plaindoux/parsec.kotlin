package lambdada.parsec.io

import lambdada.parsec.extension.fold
import lambdada.parsec.parser.Parser

abstract class Reader(open val offset: Int) {
    abstract fun next(): Pair<Char, Reader>?

}

private open class StringReader(private val source: String, override val offset: Int) : Reader(offset) {

    override fun next(): Pair<Char, Reader>? =
            source.getOrNull(offset).fold({ it to StringReader(source, offset + 1) }, { null })

}

private class StringReaderWithSkip(private val skip: Parser<Unit>, private val reader: Reader) : Reader(reader.offset) {

    override fun next(): Pair<Char, Reader>? =
            skip(reader).fold({ it.input }, { reader }).next()?.let { it.first to StringReaderWithSkip(skip, it.second) }

}

class Readers {

    companion object {
        fun new(s: String): Reader = StringReader(s, 0)
        fun skip(r: Reader, p: Parser<Unit>): Reader = StringReaderWithSkip(p, r)
    }

}