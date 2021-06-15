package com.kgbier.graphql.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ParsersTest {

    @Test
    fun integer() {
        val result = Parsers.integer.parse("123abc")
        assertEquals(123, result.match)
        assertEquals("abc", result.rest.toString())
    }

    @Test
    fun zeroOrMore() {
        var result = Parsers.zeroOrMore(Parsers.integer, separatedBy = Parsers.literal(" ")).parse("1 2 3 abc")
        assertEquals(listOf(1, 2, 3), result.match)
        assertEquals(" abc", result.rest.toString())

        result = Parsers.zeroOrMore(Parsers.integer, separatedBy = Parsers.literal(" ")).parse("abc")
        assertEquals(emptyList(), result.match)
        assertEquals("abc", result.rest.toString())
    }

    @Test
    fun oneOrMore() {
        var result = Parsers.oneOrMore(Parsers.integer, separatedBy = Parsers.literal(" ")).parse("1 2 3 abc")
        assertEquals(listOf(1, 2, 3), result.match)
        assertEquals(" abc", result.rest.toString())

        result = Parsers.oneOrMore(Parsers.integer, separatedBy = Parsers.literal(" ")).parse("abc")
        assertNull(result.match)
        assertEquals("abc", result.rest.toString())
    }
}
