package lambdada.parsec.extension

import org.junit.Assert
import org.junit.Test

class T00_Extensions {
    @Test
    fun shouldReturn1() {
        Assert.assertEquals(listOf('1').charsToInt(), 1)
    }

    @Test
    fun shouldReturn42() {
        Assert.assertEquals(listOf('4','2').charsToInt(), 42)
    }

    @Test
    fun shouldReturnMinus42() {
        Assert.assertEquals(listOf('-','4','2').charsToInt(), -42)
    }
}
