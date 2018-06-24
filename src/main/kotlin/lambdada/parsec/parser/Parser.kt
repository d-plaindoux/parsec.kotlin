package lambdada.parsec.parser

import lambdada.parsec.io.Reader

class Parser<A>(private val parser: (Reader) -> Response<A>) {
    operator fun invoke(p1: Reader): Response<A> = parser.invoke(p1)
}

//typealias Parser<A> = (Reader) -> Response<A>
//fun <A> Parser(parser: Parser<A>): Parser<A> = parser

