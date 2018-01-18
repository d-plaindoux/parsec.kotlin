package lambdada.parsec.io

import java.util.*

class Reader(val source: String, val offset: Int) {

    companion object {
        fun new(s:String) : Reader = Reader(s,0)
    }

    fun getChar(): Optional<Pair<Char, Reader>> {
        if (offset < source.length) {
            return Optional.of(Pair(source[offset], Reader(source, offset + 1)))
        }

        return Optional.empty()
    }

}
