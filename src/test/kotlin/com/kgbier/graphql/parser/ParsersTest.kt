package com.kgbier.graphql.parser

import org.junit.Test
import kotlin.test.assertEquals

internal class ParsersTest {

    @Test
    fun integer() {
        val result = Parsers.integer.parse("123abc")
        assertEquals(123, result.match)
        assertEquals("abc", result.rest.toString())
    }
}