package lambdada.parsec.parser

//
// Specific Char parsers
//

val Char.Companion.any: Parser<Char, Char> get() = any()

fun charIn(c: CharRange): Parser<Char, Char> = doTry(Char.any satisfy { c.contains(it) })

fun charIn(s: String): Parser<Char, Char> = doTry(Char.any satisfy { it in s })

fun charIn(vararg s: Char): Parser<Char, Char> = doTry(Char.any satisfy { it in s })

fun char(c: Char): Parser<Char, Char> = charIn(c)
