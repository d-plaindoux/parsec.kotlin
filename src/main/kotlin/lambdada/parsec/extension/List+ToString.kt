package lambdada.parsec.extension

//
// List data conversion
//

fun List<Char>.charsToString(): String = this.toCharArray().joinToString(separator = "")

fun List<String>.stringsToString(): String = this.joinToString(separator = "")

fun List<Char>.charsToInt(): Int = this.charsToString().toInt()

fun List<Char>.charsToFloat(): Float = this.charsToString().toFloat()
