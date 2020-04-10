package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.substring.Substring

interface Parser<T> {
    fun run(str: Substring): T?
}

fun <T> Parser<T>.parse(str: Substring) = run(str)

data class ParseResult<T>(val match: T?, val rest: Substring)

fun <T> Parser<T>.parse(str: String): ParseResult<T> {
    val substring = Substring(str)
    val match = parse(substring)
    return ParseResult(match, substring)
}