package lambdada.parsec.parser

import lambdada.parsec.io.CharReader

typealias Parser<A> = (CharReader) -> Response<A>

fun <A> Parser(p: Parser<A>) = p
