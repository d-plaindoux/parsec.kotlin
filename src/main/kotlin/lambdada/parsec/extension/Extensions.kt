package lambdada.parsec.extension

//
// Conversion
//

fun List<Char>.toInt(): Int = this.toCharArray().joinToString(separator = "").toInt()

//
// Optional catamorphism
//

fun <A, B> A?.fold(s: (A) -> B, e: () -> B): B = this?.let { s(it) } ?: e()

//
// String

fun String.next(): Pair<Char, String>? =
        if (this.isEmpty()) {
            null
        } else {
            Pair(this[0], this.substring(1))
        }
