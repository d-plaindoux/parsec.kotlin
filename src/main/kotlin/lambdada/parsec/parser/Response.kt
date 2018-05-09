package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// lambdada.parsec.Result data structure for Parser Combinator
//

sealed class Response<T, A>
data class Accept<T, A>(val value: A, val input: Reader<T>, val consumed: Boolean) : Response<T, A>()
data class Reject<T, A>(val position: Int, val consumed: Boolean) : Response<T, A>()

fun <T, A, B> Response<T, A>.fold(accept: (Accept<T, A>) -> B, reject: (Reject<T, A>) -> B): B =
        when (this) {
            is Accept -> accept(this)
            is Reject -> reject(this)
        }
