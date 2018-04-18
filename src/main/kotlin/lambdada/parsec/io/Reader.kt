package lambdada.parsec.io

import lambdada.parsec.extension.fold
import lambdada.parsec.parser.Parser

abstract class Reader<T>(open val offset: Int) {

    abstract fun next(): Pair<T, Reader<T>>?

}

private class ListReader<T>(private val source: List<T>, override val offset: Int) : Reader<T>(offset) {

    override fun next(): Pair<T, Reader<T>>? =
            source.getOrNull(offset).fold({ it to ListReader(source, offset + 1) }, { null })

}

private class ReaderWithSkip<T>(private val skip: Parser<T, *>, private val reader: Reader<T>) : Reader<T>(reader.offset) {

    override fun next(): Pair<T, Reader<T>>? =
            skip(reader).fold({ it.input }, { reader }).next()?.let { it.first to ReaderWithSkip(skip, it.second) }

}

private class ParserReader<T, A>(private val p: Parser<T, A>, private val r: Reader<T>) : Reader<A>(r.offset) {
    override fun next(): Pair<A, Reader<A>>? =
            p(r).fold({ a -> a.value to ParserReader(p, a.input) }, { null })

}

class Readers {

    companion object {
        fun fromString(s: String): Reader<Char> = ListReader(s.toList(), 0)
    }
}

infix fun <T, A> Reader<T>.tokenizeWith(r: Parser<T, A>): Reader<A> = ParserReader(r, this)
infix fun <T> Reader<T>.skip(r: Parser<T, *>): Reader<T> = ReaderWithSkip(r, this)
