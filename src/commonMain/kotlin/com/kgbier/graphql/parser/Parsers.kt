package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.Maybe
import com.kgbier.graphql.parser.substring.Substring

internal object Parsers {

    val always = object : Parser<Unit> {
        override fun run(str: Substring) = Unit
    }

    fun <A> always(a: A) = object : Parser<A> {
        override fun run(str: Substring) = a
    }

    fun <A> never() = object : Parser<A> {
        override fun run(str: Substring): A? = null
    }

    fun <A> deferred(f: () -> Parser<A>) = object : Parser<A> {
        override fun run(str: Substring): A? = f().parse(str)
    }

    fun <A> zeroOrMore(p: Parser<A>) = zeroOrMore<A, Unit>(p, null)

    fun <A, B> zeroOrMore(p: Parser<A>, separatedBy: Parser<B>?) = object : Parser<List<A>> {
        override fun run(str: Substring): List<A> {
            var remainderState = str.state
            val matches = mutableListOf<A>()
            while (true) {
                val match = p.parse(str) ?: break
                remainderState = str.state
                matches.add(match)
                if (separatedBy != null) {
                    separatedBy.parse(str) ?: return matches
                }
            }
            str.state = remainderState
            return matches
        }
    }

    fun <A> oneOrMore(p: Parser<A>) = oneOrMore<A, Unit>(p, null)

    fun <A, B> oneOrMore(p: Parser<A>, separatedBy: Parser<B>?) = zeroOrMore(p, separatedBy).flatMap {
        if (it.isEmpty()) never() else always(it)
    }

    fun <A> oneOf(ps: List<Parser<out A>>) = object : Parser<A> {
        override fun run(str: Substring): A? {
            for (p in ps) {
                val match = p.run(str)
                if (match != null) return match
            }
            return null
        }
    }

    fun <A> notOneOf(ps: List<Parser<A>>) = object : Parser<Unit> {
        override fun run(str: Substring): Unit? {
            for (p in ps) {
                val match = p.run(str)
                if (match != null) return null
            }
            return Unit
        }
    }

    fun <A> maybe(p: Parser<A>) = object : Parser<Maybe<A>> {
        override fun run(str: Substring): Maybe<A> {
            val match = p.parse(str)
            return Maybe(match)
        }
    }

    val integer = object : Parser<Int> {
        override fun run(str: Substring): Int? {
            val result = str.takeWhile { it.isDigit() }
            return if (result.isNotEmpty()) {
                str.advance(result.length)
                result.toString().toInt()
            } else null
        }
    }

    val character = object : Parser<Char> {
        override fun run(str: Substring): Char? {
            return if (str.isNotEmpty()) {
                val result = str.first()
                str.advance()
                result
            } else null
        }
    }

    fun character(char: Char) = object : Parser<Char> {
        override fun run(str: Substring): Char? {
            return if (str.isNotEmpty() && str.first() == char) {
                str.advance()
                char
            } else null
        }
    }

    fun literal(literal: String) = object : Parser<Unit> {
        override fun run(str: Substring): Unit? {
            return if (str.startsWith(literal)) {
                str.advance(literal.length)
            } else null
        }
    }

    // TODO: consider `Substring` instead of `String`?
    fun prefix(predicate: (Char) -> Boolean) = object : Parser<String> {
        override fun run(str: Substring): String? {
            val result = str.takeWhile(predicate)
            return if (result.isNotEmpty()) {
                str.advance(result.length)
                result.toString()
            } else null
        }
    }
}
