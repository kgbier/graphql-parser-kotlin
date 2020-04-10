package com.kgbier.graphql.parser.structure

data class Maybe<A>(val wrappedValue: A? = null)