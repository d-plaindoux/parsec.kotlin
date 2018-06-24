package lambdada.parsec.io

abstract class Reader(open val position: Int) {

    abstract fun next(): Pair<Char?, Reader>

    private class `From List of Characters`(private val source: List<Char>, override val position: Int) : Reader(position) {

        override fun next(): Pair<Char?, Reader> {
            val v = source.getOrNull(position)
            return when (v) {
                null -> null to this
                else -> v to `From List of Characters`(source, position + 1)
            }
        }
    }

    companion object {
        fun string(s: String): Reader = `From List of Characters`(s.toList(), 0)
    }

}
