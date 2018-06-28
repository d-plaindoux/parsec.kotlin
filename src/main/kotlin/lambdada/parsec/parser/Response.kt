package lambdada.parsec.parser

import lambdada.parsec.io.CharReader
import lambdada.parsec.utils.Location

//
// Response data structure for Parser Combinator
//

sealed class Response<A>(open val consumed: Boolean) {

    //
    // Possible Responses
    //

    data class Accept<A>(val value: A,
                         val input: CharReader,
                         override val consumed: Boolean) : Response<A>(consumed)

    data class Reject<A>(val location: Location,
                         override val consumed: Boolean) : Response<A>(consumed)

    //
    // Catamorphism
    //

    fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B =
            when (this) {
                is Accept -> accept(this)
                is Reject -> reject(this)
            }

}

//
// Class extension
//

fun <A> Response<A>.isSuccess(): Boolean = this.fold({ true }, { false })

