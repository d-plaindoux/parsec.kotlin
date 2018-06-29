package lambdada.parsec.parser

//
// Sequence
// NOTE: [do] comprehension should be better

infix fun <A, B> Parser<A>.then(p: Parser<B>): Parser<Pair<A, B>> = TODO()

//
// Alternate Then
//

infix fun <A, B> Parser<A>.thenLeft(p: Parser<B>): Parser<A> = TODO()

infix fun <A, B> Parser<A>.thenRight(p: Parser<B>): Parser<B> = TODO()

//
// Choice
//

infix fun <A> Parser<A>.or(p: Parser<A>): Parser<A> = TODO()

//
// Kleene operator, optional
//

fun <A> opt(p: Parser<A>): Parser<A?> = TODO()

// NOTE: Greedy parsers | Prefix i.e. Function vs. Method

fun <A> optRep(p: Parser<A>): Parser<List<A>> = TODO()

fun <A> rep(p: Parser<A>): Parser<List<A>> = TODO()

//
// End of stream
//

var eos: Parser<Unit> = TODO()
