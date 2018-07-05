package lambdada.parsec.examples.json

sealed class JSon

object JSonNull : JSon()
data class JSonBoolean(val v: Boolean) : JSon()
data class JSonNumber(val v: Float) : JSon()
data class JSonString(val v: String) : JSon()
data class JSonArray(val v: List<JSon>) : JSon()
data class JSonObject(val v: Map<String, JSon>) : JSon()
