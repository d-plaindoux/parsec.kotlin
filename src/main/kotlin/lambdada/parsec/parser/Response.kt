package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// Response data structure for Parser Combinator
//

sealed class Response<A>(open val consumed: Boolean) {

    data class Accept<A>(val value: A, val input: Reader, override val consumed: Boolean) : Response<A>(consumed)

    data class Reject<A>(val position: Int, override val consumed: Boolean) : Response<A>(consumed)

    // Cf. Pseudo catamorphism
    fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B =
            when (this) {
                is Accept -> accept(this)
                is Reject -> reject(this)
            }

    fun get(): A? = this.fold({ it.value }, { null })

}

// Cf. Either type with nominal and error responses
