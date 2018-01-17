package lambdada.parsec.io

import org.junit.Assert
import org.junit.Test

class T00_Reader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Reader.new("a").getChar().isPresent, true)
    }

    @Test
    fun shouldReturnNothing() {
        Assert.assertEquals(Reader.new("").getChar().isPresent, false)
    }

    @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(Reader.new("a").getChar().get().first, 'a')
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Reader.new("ab").getChar().get().second.getChar().get().first, 'b')
    }

    @Test
    fun shouldReturnAReaderReturnNothing() {
        Assert.assertEquals(Reader.new("a").getChar().get().second.getChar().isPresent, false)
    }


}