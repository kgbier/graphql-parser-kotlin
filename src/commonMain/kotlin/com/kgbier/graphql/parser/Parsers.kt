/**
 * Redundant Unit returns are necessary when running in JS.
 * Functions which do not explicitly return a Unit value will return `undefined` in JS.
 */
@file:Suppress("RedundantUnitExpression")

package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.Maybe

internal object Parsers {

    val always: Parser<Unit> = Parser { Unit }

    fun <Output> always(constant: Output): Parser<Output> = Parser { constant }

    fun <Output> never(): Parser<Output> = Parser { null }

    inline fun <Output> deferred(crossinline provideParser: () -> Parser<Output>): Parser<Output> =
        Parser { provideParser()(it) }

    fun <Output> maybe(parser: Parser<Output>): Parser<Maybe<Output>> =
        Parser { Maybe(parser(it)) }

    fun <Output> zeroOrMore(parser: Parser<Output>): Parser<List<Output>> =
        zeroOrMore<Output, Unit>(parser, null)

    fun <Output, SeparatedBy> zeroOrMore(
        parser: Parser<Output>,
        separatedBy: Parser<SeparatedBy>?,
    ): Parser<List<Output>> = Parser {
        var remainderState = it.state
        val matches = mutableListOf<Output>()
        while (true) {
            val match = parser(it) ?: break
            remainderState = it.state
            matches.add(match)
            if (separatedBy != null) {
                separatedBy(it) ?: return@Parser matches
            }
        }
        it.state = remainderState

        matches
    }

    fun <Output> oneOrMore(parser: Parser<Output>): Parser<List<Output>> =
        oneOrMore<Output, Unit>(parser, null)

    fun <Output, SeparatedBy> oneOrMore(
        parser: Parser<Output>,
        separatedBy: Parser<SeparatedBy>?,
    ): Parser<List<Output>> = zeroOrMore(parser, separatedBy).flatMap {
        if (it.isEmpty()) never() else always(it)
    }

    fun <Output> oneOf(parsers: List<Parser<out Output>>): Parser<Output> = Parser {
        for (p in parsers) {
            val match = p(it)
            if (match != null) return@Parser match
        }

        null
    }

    fun <Output> notOneOf(parsers: List<Parser<Output>>): Parser<Unit> = Parser {
        for (p in parsers) {
            val match = p(it)
            if (match != null) return@Parser null
        }

        Unit
    }

    val int: Parser<Int> = Parser {
        val result = it.takeWhile { it.isDigit() }
        if (result.isNotEmpty()) {
            it.advance(result.length)
            result.toString().toIntOrNull()
        } else null
    }

    val char: Parser<Char> = Parser {
        if (it.isNotEmpty()) {
            val result = it.first()
            it.advance()
            result
        } else null
    }

    fun character(char: Char): Parser<Char> = Parser {
        if (it.isNotEmpty() && it.first() == char) {
            it.advance()
            char
        } else null
    }

    fun literal(literal: String): Parser<Unit> = Parser {
        if (it.startsWith(literal)) {
            it.advance(literal.length)
            Unit
        } else null
    }

    inline fun predicate(
        crossinline predicate: (Char) -> Boolean,
    ): Parser<String> = Parser {
        val result = it.takeWhile(predicate)
        if (result.isNotEmpty()) {
            it.advance(result.length)
            result.toString()
        } else null
    }
}
