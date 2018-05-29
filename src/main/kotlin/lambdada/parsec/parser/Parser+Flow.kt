package lambdada.parsec.parser

//
// Flow
//

// NOTE: [do] comprehension should be better
infix fun <A, B> CharParser<A>.then(f: CharParser<B>): CharParser<Pair<A, B>> =
        this flatMap { a -> f map { b -> a to b } }

infix fun <A> CharParser<A>.or(f: CharParser<A>): CharParser<A> =
        CharParser { reader ->
            val a = this.invoke(reader)
            when (a) {
                is Reject ->
                    when (a.consumed) {
                        true -> a // Not commutative
                        false -> f.invoke(reader)
                    }
                is Accept -> a
            }
        }

//
// Alternate Then
//

infix fun <A, B> CharParser<A>.thenLeft(f: CharParser<B>): CharParser<A> =
        this then f map { it.first }

infix fun <A, B> CharParser<A>.thenRight(f: CharParser<B>): CharParser<B> =
        this then f map { it.second }

//
// Kleene operator, optional
//

fun <A> opt(p: CharParser<A>): CharParser<A?> =
        p map { it as A? } or returns<A?>(null)

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

tailrec fun <A> occurrence(p: CharParser<A>, min: Int, r: Accept<Char, List<A>>): Response<Char, List<A>> {
    val nr = p.invoke(r.input)
    return when (nr) {
        is Reject -> if (r.value.size >= min) {
            r
        } else {
            Reject(nr.position, r.consumed || nr.consumed)
        }
        is Accept ->
            occurrence(p, min, Accept(r.value + nr.value, nr.input, r.consumed || nr.consumed))
    }
}

fun <A> optRep(p: CharParser<A>): CharParser<List<A>> = CharParser {
    // opt(p then lazy { optRep(p) } map { (p, l) -> listOf(p) + l }) map { it ?: listOf() }
    occurrence(p, 0, Accept(arrayListOf(), it, false))
}

fun <A> rep(p: CharParser<A>): CharParser<List<A>> = CharParser {
    // return p then optRep(p) map { (a, b) -> listOf(a) + b }
    occurrence(p, 1, Accept(arrayListOf(), it, false))
}

//
// End of stream
//

var eos: CharParser<Unit> =
        any thenRight fails<Unit>() or returns(Unit)
