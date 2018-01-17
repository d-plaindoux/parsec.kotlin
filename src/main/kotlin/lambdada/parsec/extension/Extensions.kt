package lambdada.parsec.extension

import java.util.*

//
// Conversion
//

fun List<Char>.toInt() : Int = this.toCharArray().joinToString(separator = "").toInt()

//
// Optional catamorphism
//

fun <A, B> Optional<A>.fold(s: (A) -> B, e: () -> B): B = this.map { s(it) }.orElseGet(e)
