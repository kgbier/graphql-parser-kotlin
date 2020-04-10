package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.substring.Substring

object Parsers {

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

    fun characterOf(char: Char) = object : Parser<Char> {
        override fun run(str: Substring): Char? {
            return if (str.first() == char) {
                str.advance()
                char
            } else null
        }
    }

    fun literal(literal: String) = object : Parser<String> {
        override fun run(str: Substring): String? {
            return if (str.startsWith(literal)) {
                str.advance(literal.length)
                literal
            } else null
        }
    }

    fun prefix(predicate: (Char) -> Boolean) = object :
            Parser<String> {
        override fun run(str: Substring): String? {
            val result = str.takeWhile(predicate)
            return if (result.isNotEmpty()) {
                str.advance(result.length)
                result.toString()
            } else null
        }
    }
}
