# Parser definition 

## CharReader

- FromList
- Companion

## Response&lt;A>

- Data classes
- fold (Catamorphism) using pattern matching
- Extension

```kotlin
sealed class Response<A>(open val consumed: Boolean) {

    data class Accept<A>(val value: A, val input: CharReader, override val consumed: Boolean) : Response<A>(consumed)
    data class Reject<A>(val location: Location, override val consumed: Boolean) : Response<A>(consumed)

    fun <B> fold(accept: (Accept<A>) -> B, reject: (Reject<A>) -> B): B =
            when (this) {
                is Accept -> accept(this)
                is Reject -> reject(this)
            }
}

fun <A> Response<A>.isSuccess(): Boolean = this.fold({ true }, { false })
```

## Parser&lt;A>

- Class definition
- POO for extension

# Core Parsers

- returns
- fails
- any

# GOTO SLIDES

# Core Parser Combinators

- lazy
- doTry
- lookahead

# GOTO SLIDES

# Monadic Parser Combinators

- map
- join
- flatMap
    - INTRODUCE **infix** 
- satisfy


```kotlin
infix fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> =
        Parser { input ->
            val a = this.parse(input)
            when (a) {
                is Reject -> Reject<B>(a.location, a.consumed)
                is Accept -> Accept(f(a.value), a.input, a.consumed)
            }
        }

fun <A> join(p: Parser<Parser<A>>): Parser<A> =
        Parser { input ->
            val a = p.parse(input)
            when (a) {
                is Reject -> Reject<A>(a.location, a.consumed)
                is Accept -> {
                    val b = a.value.parse(a.input)
                    when (b) {
                        is Reject -> Reject<A>(b.location, a.consumed || b.consumed)
                        is Accept -> Accept(b.value, b.input, a.consumed || b.consumed)
                    }
                }
            }
        }

infix fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> =
        join(this map f)

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> =
        this flatMap { if (p(it)) { returns(it) } else { fails() } }
```

# GOTO SLIDES

# Type aliasing

- remove class
- define typealias
- define function Parse for compatibility

# GOTO SLIDES

# Flow Parser Combinators

- then 
- or
- opt
- optrep
- rep

```kotlin
infix fun <A, B> Parser<A>.then(p: Parser<B>): Parser<Pair<A, B>> =
        this flatMap { a -> p map { b -> a to b } }

infix fun <A> Parser<A>.or(p: Parser<A>): Parser<A> =
        Parser { reader ->
                        val a = this.parse(reader)
            when (a) {
                is Reject ->
                    when (a.consumed) {
                        true -> a // Not commutative
                        false -> p.parse(reader)
                    }
                is Accept -> a
            }
        }

fun <A> opt(p: Parser<A>): Parser<A?> =
        p map { it as A? } or returns<A?>(null)

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        opt(p then lazy { optRep(p) }) map { it?.let { listOf(it.first) + it.second } ?: listOf() }

fun <A> rep(p: Parser<A>): Parser<List<A>> =
        p then optRep(p) map { r -> listOf(r.first) + r.second }
```

- Activate test with "large" String 

# Tail Recursion

```kotlin
private tailrec fun <A> optRep(p: Parser<A>, acc: List<A>, consumed: Boolean, charReader: CharReader): Response<List<A>> {
    val a = p.parse(charReader)
    return when (a) {
        is Reject -> Accept(acc, charReader, consumed)
        is Accept -> optRep(p, acc + a.value, consumed || a.consumed, a.input)
    }
}

fun <A> optRep(p: Parser<A>): Parser<List<A>> =
        Parser { optRep(p, listOf(), false, it) }
```

# Char parsers

- char
- charIn

