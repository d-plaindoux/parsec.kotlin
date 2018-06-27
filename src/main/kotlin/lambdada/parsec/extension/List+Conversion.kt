package lambdada.parsec.extension

fun List<Char>.charsToString(): String = this.toCharArray().joinToString(separator = "")
fun List<String>.stringsToString(): String = this.joinToString(separator = "")
fun List<Char>.charsToInt(): Int = this.charsToString().toInt()
fun List<Char>.charsToFloat(): Float = this.charsToString().toFloat()

infix fun <A, B, C> ((B) -> C).compose(f: (A) -> B): (A) -> C = { this(f(it)) }
infix fun <A, B, C> ((A) -> B).pipe(f: (B) -> C): (A) -> C = f compose this
