package lambdada.parsec.io

import org.junit.Assert
import org.junit.Test

class T00_Reader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Readers.fromString("a").next().first?.let { true } ?: false, true)
    }

    @Test
    fun shouldReturnNothing() {
        Assert.assertEquals(Readers.fromString("").next().first?.let { false } ?: true, true)
    }

    @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(Readers.fromString("a").next().first, 'a')
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Readers.fromString("ab").next().second.next().first, 'b')
    }

    @Test
    fun shouldReturnAReaderReturnNothing() {
        Assert.assertEquals(Readers.fromString("a").next().second.next().first?.let { true } ?: false, false)
    }

}