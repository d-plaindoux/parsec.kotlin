package lambdada.parsec.parser

import lambdada.parsec.io.Reader
import org.junit.Assert
import org.junit.Test

class T00_Response {

    @Test
    fun shouldConsumedAcceptResponseMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), true).map { it + 2 }.fold({ it.value == 3 && it.consumed }, { false }), true)
    }

    @Test
    fun shouldNotConsumedAcceptResponseMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), false).map { it + 2 }.fold({ it.value == 3 && !it.consumed }, { false }), true)
    }

    @Test
    fun shouldConsumedAcceptResponseFlatMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), true).flatMap { Accept(it + 2, Reader.new(""), true) }.fold({ it.value == 3 && it.consumed }, { false }), true)
    }

    @Test
    fun shouldNotConsumedAndConsumedAcceptResponseFlatMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), false).flatMap { Accept(it + 2, Reader.new(""), true) }.fold({ it.value == 3 && it.consumed }, { false }), true)
    }

    @Test
    fun shouldConsumedAndNotConsumedAcceptResponseFlatMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), true).flatMap { Accept(it + 2, Reader.new(""), false) }.fold({ it.value == 3 && !it.consumed }, { false }), true)
    }

    @Test
    fun shouldNotConsumedAndNotConsumedAcceptResponseFlatMap() {
        Assert.assertEquals(Accept(1, Reader.new(""), false).flatMap { Accept(it + 2, Reader.new(""), true) }.fold({ it.value == 3 && it.consumed }, { false }), true)
    }
}