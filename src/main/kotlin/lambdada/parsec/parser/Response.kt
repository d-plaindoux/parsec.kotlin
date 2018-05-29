package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// lambdada.parsec.Result data structure for Parser Combinator
//

sealed class Response<T, A>(open val consumed: Boolean)
data class Accept<T, A>(val value: A, val input: Reader<T>, override val consumed: Boolean) : Response<T, A>(consumed)
data class Reject<T, A>(val position: Int, override val consumed: Boolean) : Response<T, A>(consumed)

fun <T, A, B> Response<T, A>.fold(accept: (Accept<T, A>) -> B, reject: (Reject<T, A>) -> B): B =
        when (this) {
            is Accept -> accept(this)
            is Reject -> reject(this)
        }
