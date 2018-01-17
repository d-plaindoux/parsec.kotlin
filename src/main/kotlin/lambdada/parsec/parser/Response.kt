package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// lambdada.parsec.Result data structure for Parser Combinator
//

// No Monad definition? Back to naive OO approach!

sealed class Response<A> {
    abstract fun <B> map(f: (A) -> B): Response<B>
    abstract fun <B> flatMap(f: (A) -> Response<B>): Response<B>
    abstract fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B
}

class Accept<A>(val value: A, val input: Reader, val consumed: Boolean) : Response<A>() {
    override fun <B> map(f: (A) -> B): Response<B> = Accept<B>(f(value), input, consumed)
    override fun <B> flatMap(f: (A) -> Response<B>): Response<B> = f(value)
    override fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B = accept(this)

    override fun toString(): String = "Accept($value)"
}

class Reject<A>(val consumed: Boolean) : Response<A>() {
    override fun <B> map(f: (A) -> B): Response<B> = Reject<B>(consumed)
    override fun <B> flatMap(f: (A) -> Response<B>): Response<B> = Reject<B>(consumed)
    override fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B = reject(this)

    override fun toString(): String = "Reject()"
}
