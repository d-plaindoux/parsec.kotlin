package lambdada.parsec.parser

import lambdada.parsec.io.CharReader

typealias Parser<A> = (CharReader) -> Response<A>
