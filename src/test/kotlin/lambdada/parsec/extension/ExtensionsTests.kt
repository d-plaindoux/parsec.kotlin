package lambdada.parsec.extension

import org.junit.Assert
import org.junit.Test

class ExtensionsTests {
    @Test
    fun shouldReturn1() {
        Assert.assertEquals(listOf('1').toInt(), 1)
    }

    @Test
    fun shouldReturn42() {
        Assert.assertEquals(listOf('4','2').toInt(), 42)
    }

    @Test
    fun shouldReturnMinus42() {
        Assert.assertEquals(listOf('-','4','2').toInt(), -42)
    }
}
