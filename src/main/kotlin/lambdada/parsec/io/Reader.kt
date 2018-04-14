package lambdada.parsec.io

import lambdada.parsec.extension.next

class Reader(private val source: String, val offset: Int) {

    companion object {
        fun new(s: String): Reader = Reader(s, 0)
    }

    fun next(): Pair<Char, Reader>? =
            source.next()?.let { (c, s) -> c to Reader(s, offset + 1) }

}
