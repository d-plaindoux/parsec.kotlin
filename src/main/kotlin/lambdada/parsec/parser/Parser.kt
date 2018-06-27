package lambdada.parsec.parser

import lambdada.parsec.io.CharReader

class Parser<A>(private val parser: (CharReader) -> Response<A>) {
    fun parse(charReader: CharReader): Response<A> = parser(charReader)
}

//typealias Parser<A> = (CharReader) -> Response<A>
//
//fun <A> Parser(parser: Parser<A>): Parser<A> = parser

