package lambdada.parsec.io

import lambdada.parsec.utils.Location
import java.net.URL

interface Reader<A> {

    fun location(): Location
    fun canRead(): Boolean
    fun read(): Pair<A, Reader<A>>

    // CharReader using a list of characters
    private class FromList(private val source: List<Char>,
                           private val position: Int) : Reader<Char> {
        override fun location() = Location(position)
        override fun canRead() = position < source.count()
        override fun read() = source[position] to FromList(source, position + 1)
    }

    // Companion object
    companion object {
        fun string(s: String): Reader<Char> = FromList(s.toList(), 0)
        fun url(s: URL): Reader<Char> = string(s.readText())
    }

}
