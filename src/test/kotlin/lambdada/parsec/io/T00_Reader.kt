package lambdada.parsec.io

import lambdada.parsec.extension.fold
import org.junit.Assert
import org.junit.Test

class T00_Reader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Readers.fromString("a").next().fold({ true }, { false }), true)
    }

    @Test
    fun shouldReturnNothing() {
        Assert.assertEquals(Readers.fromString("").next().fold({ true }, { false }), false)
    }

    @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(Readers.fromString("a").next()?.first, 'a')
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Readers.fromString("ab").next()?.second?.next()?.first, 'b')
    }

    @Test
    fun shouldReturnAReaderReturnNothing() {
        Assert.assertEquals(Readers.fromString("a").next()?.second?.next().fold({ true }, { false }), false)
    }

}