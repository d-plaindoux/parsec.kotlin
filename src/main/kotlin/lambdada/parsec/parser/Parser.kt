package lambdada.parsec.parser

import lambdada.parsec.io.Reader

typealias Parser<I, A> = (Reader<I>) -> Response<I, A>
