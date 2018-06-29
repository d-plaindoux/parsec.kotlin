package lambdada.parsec.parser

//
// Parser providing pseudo-Monadic ADT
//

// p.map(...)
infix fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> = TODO()

fun <A> join(p: Parser<Parser<A>>): Parser<A> = TODO()

infix fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> = TODO()

//
// Filtering
//

infix fun <A> Parser<A>.satisfy(p: (A) -> Boolean): Parser<A> = TODO()

//
// Applicative
// Warning: In the monadic combinator, the second parser always depends
//          on the result of the first parser.
//

fun <A, B> Parser<A>.applicative(p: Parser<(A) -> B>): Parser<B> = TODO()

//
// Kliesli
//

fun <A, B, C> ((A) -> Parser<B>).then(p2: (B) -> Parser<C>): (A) -> Parser<C> = TODO()
