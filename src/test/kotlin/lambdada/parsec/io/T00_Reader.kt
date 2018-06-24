package lambdada.parsec.io

import org.junit.Assert
import org.junit.Test

class T00_Reader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Reader.string("a").next().first?.let { true } ?: false, true)
    }

    @Test
    fun shouldReturnNothing() {
        Assert.assertEquals(Reader.string("").next().first?.let { false } ?: true, true)
    }

    @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(Reader.string("a").next().first, 'a')
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Reader.string("ab").next().second.next().first, 'b')
    }

    @Test
    fun shouldReturnAReaderReturnNothing() {
        Assert.assertEquals(Reader.string("a").next().second.next().first?.let { true } ?: false, false)
    }

}