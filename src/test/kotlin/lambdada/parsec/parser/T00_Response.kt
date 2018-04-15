package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T00_Response {

    @Test
    fun shouldConsumedAcceptResponseMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), true).map { it + 2 }.fold({ it.value == 3 && it.consumed }, { false }), true)
    }

    @Test
    fun shouldNotConsumedAcceptResponseMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), false).map { it + 2 }.fold({ it.value == 3 && !it.consumed }, { false }), true)
    }

}