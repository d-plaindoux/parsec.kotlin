package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// lambdada.parsec.Result data structure for Parser Combinator
//

data class Accept<T, A>(val value: A, val input: Reader<T>, val consumed: Boolean) : Response<T, A>()
data class Reject<T, A>(val position: Int, val consumed: Boolean) : Response<T, A>()

sealed class Response<T, A> {

    /**
     * Catamorphism
     */
    fun <B> fold(accept: (Accept<T, A>) -> B, reject: (Reject<T, A>) -> B): B =
            when (this) {
                is Accept<*, *> -> accept(this as Accept<T, A>)
                is Reject<*, *> -> reject(this as Reject<T, A>)
            }

    /**
     * Function mapping also called map
     */
    fun <B> map(f: (A) -> B): Response<T, B> = fold(
            { Accept(f(it.value), it.input, it.consumed) },
            { Reject(it.position, it.consumed) }
    )

}
