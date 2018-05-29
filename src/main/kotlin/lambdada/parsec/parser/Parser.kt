package lambdada.parsec.parser

import lambdada.parsec.io.Reader

//
// Parser type definition
//

typealias Parser<T, A> = (Reader<T>) -> Response<T, A>


typealias CharParser<A> = (Reader<Char>) -> Response<Char, A>

fun <A> CharParser(parser: Parser<Char, A>): CharParser<A> {
    return parser
}

/*
class CharParser<A>(private val parser: Parser<Char, A>) {

    operator fun invoke(p1: Reader<Char>): Response<Char, A> = parser.invoke(p1)

}
*/
