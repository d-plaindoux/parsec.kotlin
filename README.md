# Parser Combinator in Kotlin

[![Build Status](https://travis-ci.org/d-plaindoux/parsec.kotlin.svg?branch=master)](https://travis-ci.org/d-plaindoux/parsec.kotlin)
[![unstable](http://badges.github.io/stability-badges/dist/unstable.svg)](http://github.com/badges/stability-badges)

# Objective 

A [parser combinator library](https://www.microsoft.com/en-us/research/wp-content/uploads/2016/02/parsec-paper-letter.pdf)
implementation from scratch in [Kotlin](https://kotlinlang.org).

# Examples

## CSV

```kotlin
// item    ::= [^,]*
// csvline ::= item (',' item)*

var item     = optrep(not(char(','))) 
val csvline  = item then optrep(char(',') then item)
```

## Expressions

```kotlin
// SEXPR ::= '(' EXPR ')' | FLOAT
// EXPR  ::= SEPXR (('+'|'*') EXPR)?

fun SEXPR() =
    char('(') then lazy { EXPR() } then char(')') or FLOAT

fun EXPR() =
    lazy { SEXPR() then opt(charIn('+', '*') then EXPR()) }
```

# Why this library

This material has been designed for a [Lambdada](http://lambdada.org) 
session in order to explore the Kotlin language implementing a library
with intensive function composition.  

# License

Copyright 2018 D. Plaindoux.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
