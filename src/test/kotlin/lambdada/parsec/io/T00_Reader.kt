package lambdada.parsec.io

import lambdada.parsec.extension.fold
import org.junit.Assert
import org.junit.Test

class T00_Reader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Readers.new("a").next().fold({ true }, { false }), true)
    }

    @Test
    fun shouldReturnNothing() {
        Assert.assertEquals(Readers.new("").next().fold({ true }, { false }), false)
    }

    @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(Readers.new("a").next()?.first, 'a')
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Readers.new("ab").next()?.second?.next()?.first, 'b')
    }

    @Test
    fun shouldReturnAReaderReturnNothing() {
        Assert.assertEquals(Readers.new("a").next()?.second?.next().fold({ true }, { false }), false)
    }

}