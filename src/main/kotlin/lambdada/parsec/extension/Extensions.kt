package lambdada.parsec.extension

//
// List conversion
//


fun List<Char>.charsToString(): String = this.toCharArray().joinToString(separator = "")

fun List<String>.stringsToString(): String = this.joinToString(separator = "")

fun List<Char>.charsToInt(): Int = this.charsToString().toInt()

//
// Optional catamorphism
//

fun <A, B> A?.fold(s: (A) -> B, e: () -> B): B = this?.let { s(it) } ?: e()

//
// String split

fun String.next(): Pair<Char, String>? =
        if (this.isEmpty()) {
            null
        } else {
            Pair(this[0], this.substring(1))
        }
