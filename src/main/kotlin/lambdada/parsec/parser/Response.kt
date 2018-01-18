package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// lambdada.parsec.Result data structure for Parser Combinator
//

// No Monad definition? Back to naive OO approach!

interface Response<A> {
    fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B

    fun <B> flatMap(f: (A) -> Response<B>): Response<B> = fold({ f(it.value) }, { Reject(it.position, it.consumed) })
    fun <B> map(f: (A) -> B): Response<B> = fold({ Accept(f(it.value), it.input, it.consumed) }, { Reject(it.position, it.consumed) })
}

data class Accept<A>(val value: A, val input: Reader, val consumed: Boolean) : Response<A> {
    override fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B = accept(this)

    override fun toString(): String = "Accept($value)"
}

data class Reject<A>(val position: Int, val consumed: Boolean) : Response<A> {
    override fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B = reject(this)

    override fun toString(): String = "Reject($position)"
}
