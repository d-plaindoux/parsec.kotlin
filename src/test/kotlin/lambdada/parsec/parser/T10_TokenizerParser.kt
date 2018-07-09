package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import lambdada.parsec.io.tokenize
import org.junit.Assert
import org.junit.Test

class T10_TokenizerParser {

    private fun <A> Response<*, A>.get(): A? = this.fold({ it.value }, { null })

    @Test
    fun shouldCountNumber() {
        val tokenizer = INTEGER thenLeft char(',').opt
        val reader = Reader.string("42,43").tokenize(tokenizer)

        val parser = any<Int>().rep thenLeft eos()

        Assert.assertEquals(2, parser(reader).get()?.size ?: -1)
    }


}