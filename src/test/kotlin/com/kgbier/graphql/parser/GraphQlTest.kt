package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.Value
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class GraphQlTest {

    private val graphQlparser = GraphQl()

    @Test
    fun name() {
        fun testSubject(str: String): String? = graphQlparser.name.parse(str).match

        assertEquals("abc", testSubject("abc"))
        assertEquals("abc", testSubject("abc"))
        assertEquals("a123", testSubject("a123"))
        assertEquals("with_underscore_", testSubject("with_underscore_"))
        assertEquals("__typename", testSubject("__typename"))
        assertNull(testSubject("123"))
    }

    @Test
    fun negativeSign() {
        fun testSubject(str: String): Char? = graphQlparser.negativeSign.parse(str).match

        assertEquals('-', testSubject("-"))
        assertNull(testSubject("a"))
        assertNull(testSubject("1"))
        assertNull(testSubject(" "))
    }

    @Test
    fun digit() {
        fun testSubject(str: String): Char? = graphQlparser.digit.parse(str).match

        assertEquals('0', testSubject("0"))
        assertEquals('1', testSubject("1"))
        assertEquals('2', testSubject("2"))
        assertEquals('3', testSubject("3"))
        assertEquals('4', testSubject("4"))
        assertEquals('5', testSubject("5"))
        assertEquals('6', testSubject("6"))
        assertEquals('7', testSubject("7"))
        assertEquals('8', testSubject("8"))
        assertEquals('9', testSubject("9"))
    }

    @Test
    fun nonZeroDigit() {
        fun testSubject(str: String): Char? = graphQlparser.nonZeroDigit.parse(str).match

        assertNull(testSubject("0"))
        assertEquals('1', testSubject("1"))
        assertEquals('1', testSubject("1"))
        assertEquals('2', testSubject("2"))
        assertEquals('3', testSubject("3"))
        assertEquals('4', testSubject("4"))
        assertEquals('5', testSubject("5"))
        assertEquals('6', testSubject("6"))
        assertEquals('7', testSubject("7"))
        assertEquals('8', testSubject("8"))
        assertEquals('9', testSubject("9"))
    }

    // alias: integerPart
    @Test
    fun intValue() {
        fun testSubject(str: String): Value? = graphQlparser.intValue.parse(str).match

        assertEquals(Value.ValueInt("123"), testSubject("123"))
        assertEquals(Value.ValueInt("-123"), testSubject("-123"))
        assertNull(testSubject("0123"))
        assertEquals(Value.ValueInt("-0"), testSubject("-0"))
    }

    @Test
    fun exponentIndicator() {
        fun testSubject(str: String): Unit? = graphQlparser.exponentIndicator.parse(str).match

        assertNotNull(testSubject("E"))
        assertNotNull(testSubject("e"))
        assertNull(testSubject("a"))
        assertNull(testSubject("1"))
        assertNull(testSubject(" "))
    }

    @Test
    fun sign() {
        fun testSubject(str: String): Char? = graphQlparser.sign.parse(str).match

        assertEquals('+', testSubject("+"))
        assertEquals('-', testSubject("-"))
        assertNull(testSubject("a"))
        assertNull(testSubject("1"))
        assertNull(testSubject(" "))
    }

    @Test
    fun fractionalPart() {
        fun testSubject(str: String): String? = graphQlparser.fractionalPart.parse(str).match

        assertEquals("123", testSubject(".123"))
        assertEquals("012", testSubject(".012"))
        assertNull(testSubject("012"))
        assertNull(testSubject("."))
        assertNull(testSubject("a"))
    }

    @Test
    fun exponentPart() {
        fun testSubject(str: String): String? = graphQlparser.exponentPart.parse(str).match

        assertEquals("e+123", testSubject("e123"))
        assertEquals("e+123", testSubject("e+123"))
        assertEquals("e-123", testSubject("e-123"))
        assertNull(testSubject("e"))
        assertNull(testSubject("e+"))
        assertNull(testSubject("e-"))
        assertNull(testSubject("E"))
        assertNull(testSubject("E+"))
        assertNull(testSubject("E-"))
        assertNull(testSubject("a"))
        assertNull(testSubject("1"))
        assertNull(testSubject(" "))
    }

    @Test
    fun floatValue() {
        fun testSubject(str: String): Value? = graphQlparser.floatValue.parse(str).match

        assertEquals(Value.ValueFloat("6.0221413:e+23"), testSubject("6.0221413e23"))
        assertEquals(Value.ValueFloat("6.123"), testSubject("6.123"))
        assertEquals(Value.ValueFloat("1:e+10"), testSubject("1e10"))
    }

    @Test
    fun booleanValue() {
        fun testSubject(str: String): Value? = graphQlparser.booleanValue.parse(str).match

        assertEquals(Value.ValueBoolean(true), testSubject("true"))
        assertEquals(Value.ValueBoolean(false), testSubject("false"))
        assertNull(testSubject("a"))
        assertNull(testSubject("1"))
        assertNull(testSubject(" "))
    }

    @Test
    fun stringValue() {
        fun testSubject(str: String): Value? = graphQlparser.stringValue.parse(str).match

        assertEquals(Value.ValueString("hello sailor"), testSubject("\"hello sailor\""))
        assertEquals(Value.ValueString(" hello sailor "), testSubject("\" hello sailor \""))
        assertEquals(Value.ValueString("123 abc"), testSubject("\"123 abc\""))
        assertEquals(Value.ValueString(""), testSubject("\"\""))
        assertNull(testSubject("hello sailor\""))
        assertNull(testSubject("\"hello sailor"))
    }

    @Test
    fun nullValue() {
        fun testSubject(str: String): Value? = graphQlparser.nullValue.parse(str).match

        assertEquals(Value.ValueNull, testSubject("null"))
        assertNull(testSubject("012"))
        assertNull(testSubject("."))
        assertNull(testSubject("a"))
    }

    @Test
    fun enumValue() {
        fun testSubject(str: String): Value? = graphQlparser.enumValue.parse(str).match

        assertEquals(Value.ValueEnum("abc"), testSubject("abc"))
        assertEquals(Value.ValueEnum("abc123"), testSubject("abc123"))
        assertEquals(Value.ValueEnum("ENUM_VALUE"), testSubject("ENUM_VALUE"))
        assertNull(testSubject("true"))
        assertNull(testSubject("false"))
        assertNull(testSubject("null"))
        assertNull(testSubject("123"))
    }

    @Test
    fun listValue() {
        fun testSubject(str: String): Value? = graphQlparser.listValue.parse(str).match

        assertEquals(Value.ValueList(listOf()), testSubject("[ ]"))
        assertEquals(Value.ValueList(listOf()), testSubject("[]"))
        assertEquals(Value.ValueList(listOf()), testSubject("[ , ]"))
        assertEquals(Value.ValueList(listOf(Value.ValueBoolean(true))), testSubject("[true]"))
        assertEquals(Value.ValueList(listOf(Value.ValueBoolean(true), Value.ValueBoolean(false))), testSubject("[ true, false ]"))
    }

}