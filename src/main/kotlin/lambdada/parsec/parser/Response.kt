package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// lambdada.parsec.Result data structure for Parser Combinator
//

data class Accept<A>(val value: A, val input: Reader, val consumed: Boolean) : Response<A>()
data class Reject<A>(val position: Int, val consumed: Boolean) : Response<A>()

sealed class Response<A> {

    /**
     * Catamorphism
     */
    fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B =
            when (this) {
                is Accept<*> -> accept(this as Accept<A>)
                is Reject<*> -> reject(this as Reject<A>)
            }

    /**
     * Function mapping also called map
     */
    fun <B> map(f: (A) -> B): Response<B> = fold(
            { Accept(f(it.value), it.input, it.consumed) },
            { Reject(it.position, it.consumed) }
    )
}
