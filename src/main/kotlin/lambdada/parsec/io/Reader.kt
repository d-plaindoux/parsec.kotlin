package lambdada.parsec.io

import lambdada.parsec.extension.fold

class Reader(private val source: String, val offset: Int) {

    companion object {
        fun new(s: String): Reader = Reader(s, 0)
    }

    fun next(): Pair<Char, Reader>? = source.getOrNull(offset).fold({ it to Reader(source, offset + 1) }, { null })
}
