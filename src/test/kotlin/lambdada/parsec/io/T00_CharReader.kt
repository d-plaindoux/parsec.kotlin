package lambdada.parsec.io

import org.junit.Assert
import org.junit.Test

class T00_Reader {

    @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Reader.string("a").read()?.first, 'a')
    }

    @Test
    fun shouldHasNextFails() {
        Assert.assertEquals(Reader.string("").read(), null)
    }

    @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Reader.string("ab").read()?.second?.read()?.first, 'b')
    }

}