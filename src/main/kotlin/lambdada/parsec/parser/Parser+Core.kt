package lambdada.parsec.parser

//
// Basic parsers
//

fun <A> returns(v: A, consumed: Boolean = false): CharParser<A> =
        CharParser { Accept(v, it, consumed) }

fun <A> fails(consumed: Boolean = false): CharParser<A> =
        CharParser<A> { Reject(it.offset, consumed) }

//
// Parser providing pseudo-Monadic ADT
//

infix fun <A, B> CharParser<A>.flatMap(f: (A) -> CharParser<B>): CharParser<B> =
        CharParser<B> { input ->
            val a = this.invoke(input)
            when (a) {
                is Reject -> Reject(a.position, a.consumed)
                is Accept -> {
                    val b = f(a.value).invoke(a.input)
                    when (b) {
                        is Reject -> Reject(b.position, a.consumed || b.consumed)
                        is Accept -> Accept(b.value, b.input, a.consumed || b.consumed)
                    }
                }
            }
        }

infix fun <A, B> CharParser<A>.map(f: (A) -> B): CharParser<B> =
        this flatMap { returns(f(it)) }

// p flatMap f == join (p map f) -- How can we express join?

//
// Element parser
//

var any: CharParser<Char> = CharParser {
    val (c, input) = it.next()
    when (c) {
        null -> fails<Char>(false).invoke(it)
        else -> returns(c, true).invoke(input)
    }
}

//
// Filtering
//

infix fun <A> CharParser<A>.satisfy(p: (A) -> Boolean): CharParser<A> =
        this flatMap { if (p(it)) returns(it) else fails() }

//
// Lazy parser
//

fun <A> lazy(f: () -> CharParser<A>): CharParser<A> =
        CharParser { f().invoke(it) }

//
// Backtracking
//

fun <A> doTry(p: CharParser<A>): CharParser<A> = CharParser<A> {
    val a = p.invoke(it)
    when (a) {
        is Reject -> Reject(a.position, false)
        is Accept -> a
    }
}

//
// Lookahead
//

fun <A> lookahead(p: CharParser<A>): CharParser<A> = CharParser<A> {
    val a = p.invoke(it)
    when (a) {
        is Reject -> fails<A>().invoke(it)
        is Accept -> returns(a.value, false).invoke(it)
    }
}

//
// Applicative context sensitive
//

infix fun <A, B> CharParser<A>.wrong_applicative(f: CharParser<(A) -> B>): CharParser<B> =
        f flatMap { this map it }

infix fun <A, B> CharParser<A>.applicative(f: CharParser<(A) -> B>): CharParser<B> =
        this flatMap { f map { f -> f(it) } }

//
// Kliesli monads pipelining
//

infix fun <A, B, C> ((A) -> CharParser<B>).and(f: (B) -> CharParser<C>): (A) -> CharParser<C> =
        { this(it) flatMap f }
