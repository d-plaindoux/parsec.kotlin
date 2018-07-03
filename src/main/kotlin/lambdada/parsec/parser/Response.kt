package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import lambdada.parsec.utils.Location

//
// Response data structure for Parser Combinator
//

sealed class Response<I, A>(open val consumed: Boolean) {

    //
    // Possible Responses
    //

    data class Accept<I, A>(val value: A,
                            val input: Reader<I>,
                            override val consumed: Boolean) : Response<I, A>(consumed)

    data class Reject<I, A>(val location: Location,
                            override val consumed: Boolean) : Response<I, A>(consumed)

    //
    // Catamorphism
    //

    fun <B> fold(accept: (Accept<I, A>) -> B, reject: (Reject<I, A>) -> B): B =
            when (this) {
                is Accept -> accept(this)
                is Reject -> reject(this)
            }

}

//
// Class extension
//

fun <A> Response<*, A>.isSuccess(): Boolean = this.fold({ true }, { false })

