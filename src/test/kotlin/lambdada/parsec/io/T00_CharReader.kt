package lambdada.parsec.io

import org.junit.Assert
import org.junit.Test

class T00_CharReader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(CharReader.string("a").read().first, 'a')
    }

    @Test
    fun shouldHasNextFails() {
        Assert.assertEquals(CharReader.string("").canRead(), false)
    }

    @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(CharReader.string("a").read().first, 'a')
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(CharReader.string("ab").read().second.read().first, 'b')
    }

    @Test
    fun shouldReturnAReaderHasNextFails() {
        Assert.assertEquals(CharReader.string("a").read().second.canRead(), false)
    }

}