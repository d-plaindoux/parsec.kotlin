package lambdada.parsec.io

import org.junit.Assert

class T00_Reader {

    // @Test
    fun shouldReturnSomething() {
        Assert.assertEquals(Readers.string("a").next().first?.let { true } ?: false, true)
    }

    // @Test
    fun shouldReturnNothing() {
        Assert.assertEquals(Readers.string("").next().first?.let { false } ?: true, true)
    }

    // @Test
    fun shouldReturnAChar() {
        Assert.assertEquals(Readers.string("a").next().first, 'a')
    }

    // @Test
    fun shouldReturnAReaderReturnSomething() {
        Assert.assertEquals(Readers.string("ab").next().second.next().first, 'b')
    }

    // @Test
    fun shouldReturnAReaderReturnNothing() {
        Assert.assertEquals(Readers.string("a").next().second.next().first?.let { true } ?: false, false)
    }

}