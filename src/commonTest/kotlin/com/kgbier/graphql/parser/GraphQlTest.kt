package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.*
import kotlin.test.Test
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

    @Test
    fun objectField() {
        fun testSubject(str: String): ObjectField? = graphQlparser.objectField.parse(str).match

        assertEquals(ObjectField("name", Value.ValueInt("123")), testSubject("name : 123"))
        assertEquals(ObjectField("name", Value.ValueString("abc")), testSubject("name : \"abc\""))
    }

    @Test
    fun objectValue() {
        fun testSubject(str: String): Value? = graphQlparser.objectValue.parse(str).match

        assertEquals(Value.ValueObject(emptyList()), testSubject("{}"))
        assertEquals(Value.ValueObject(emptyList()), testSubject("{ }"))
        assertEquals(Value.ValueObject(emptyList()), testSubject("{ , }"))
        assertEquals(Value.ValueObject(listOf(ObjectField("name", Value.ValueInt("123")))), testSubject("{name:123}"))
        assertEquals(Value.ValueObject(listOf(ObjectField("name", Value.ValueInt("123")))), testSubject("{ name : 123 }"))
        assertEquals(
            Value.ValueObject(listOf(ObjectField("nameone", Value.ValueInt("123")), ObjectField("nametwo", Value.ValueString("abc")))),
            testSubject("{ nameone : 123, nametwo : \"abc\" }")
        )
    }

    @Test
    fun listType() {
        fun testSubject(str: String): String? = graphQlparser.listType.parse(str).match

        assertEquals("[Int]", testSubject("[Int]"))
        assertEquals("[Int]", testSubject("[ Int ]"))
        assertEquals("[[Char]]", testSubject("[ [ Char ] ]"))
        assertNull(testSubject("[ ]"))
        assertNull(testSubject("[]"))
        assertNull(testSubject("[ , ]"))
    }

    @Test
    fun nonNullType() {
        fun testSubject(str: String): String? = graphQlparser.nonNullType.parse(str).match

        assertEquals("[Int]!!", testSubject("[Int]!"))
        assertEquals("Int!!", testSubject("Int!"))
        assertEquals("[Int!!]!!", testSubject("[Int!]!"))
    }

    @Test
    fun defaultValue() {
        fun testSubject(str: String): Value? = graphQlparser.defaultValue.parse(str).match

        assertEquals(Value.ValueString("abc"), testSubject("= \"abc\""))
        assertEquals(Value.ValueInt("123"), testSubject("=123"))
    }

    @Test
    fun variable() {
        fun testSubject(str: String): String? = graphQlparser.variable.parse(str).match

        assertEquals("abc", testSubject("\$abc"))
        assertNull(testSubject("abc"))
    }

    @Test
    fun variableDefinition() {
        fun testSubject(str: String): VariableDefinition? = graphQlparser.variableDefinition.parse(str).match

        assertEquals(VariableDefinition("abc", "Int", null), testSubject("\$abc : Int"))
        assertEquals(VariableDefinition("abc", "Int", Value.ValueInt("123")), testSubject("\$abc : Int = 123"))
        assertEquals(VariableDefinition("abc", "Int", Value.ValueInt("123")), testSubject("\$abc:Int=123"))
        assertEquals(VariableDefinition("abc", "[Int]", null), testSubject("\$abc:[Int]"))
        assertEquals(VariableDefinition("abc", "[Int!!]", null), testSubject("\$abc:[Int!]"))
        assertEquals(VariableDefinition("abc", "[Int]!!", null), testSubject("\$abc:[Int]!"))
        assertEquals(VariableDefinition("abc", "[Int!!]!!", null), testSubject("\$abc:[Int!]!"))
    }

    @Test
    fun argument() {
        fun testSubject(str: String): Argument? = graphQlparser.argument.parse(str).match

        assertEquals(Argument("abc", Value.ValueString("xyz")), testSubject("abc : \"xyz\""))
        assertEquals(Argument("abc", Value.ValueInt("123")), testSubject("abc : 123"))
        assertEquals(Argument("abc", Value.ValueInt("123")), testSubject("abc:123"))
    }

    @Test
    fun arguments() {
        fun testSubject(str: String): List<Argument>? = graphQlparser.arguments.parse(str).match

        assertEquals(listOf(Argument("abc", Value.ValueString("xyz"))), testSubject("(abc:\"xyz\")"))
        assertEquals(listOf(Argument("abc", Value.ValueInt("123"))), testSubject("(abc:123)"))
        assertEquals(listOf(Argument("abc", Value.ValueInt("123"))), testSubject("( abc : 123 )"))
        assertEquals(
            listOf(Argument("abc", Value.ValueInt("123")), Argument("def", Value.ValueString("xyz"))),
            testSubject("(abc : 123, def : \"xyz\")")
        )
    }

    @Test
    fun directive() {
        fun testSubject(str: String): Directive? = graphQlparser.directive.parse(str).match

        assertEquals(
            Directive("named", listOf(Argument("abc", Value.ValueString("xyz")))),
            testSubject("@named (abc : \"xyz\")")
        )
        assertEquals(
            Directive("named", listOf(Argument("abc", Value.ValueString("xyz")))),
            testSubject("@named(abc : \"xyz\")")
        )
    }

    @Test
    fun directives() {
        fun testSubject(str: String): List<Directive>? = graphQlparser.directives.parse(str).match

        assertEquals(
            listOf(
                Directive("named", listOf(Argument("abc", Value.ValueString("xyz")))),
                Directive("other", listOf(Argument("abc", Value.ValueString("xyz"))))
            ),
            testSubject("@named (abc : \"xyz\") @other (abc : \"xyz\")")
        )
    }

    @Test
    fun selection() {
        fun testSubject(str: String): List<Selection>? = graphQlparser.selectionSet.parse(str).match

        assertEquals(
            listOf(
                SelectionField(Field(null, "abc", emptyList(), emptyList(), emptyList())),
                SelectionField(Field(null, "def", emptyList(), emptyList(), emptyList())),
                SelectionField(Field(null, "xyz", emptyList(), emptyList(), emptyList()))
            ),
            testSubject("{abc def,xyz}")
        )
        assertEquals(
            listOf(
                SelectionField(Field(null, "abc", emptyList(), emptyList(), emptyList())),
                SelectionField(Field(null, "def", emptyList(), emptyList(), emptyList())),
                SelectionField(Field(null, "xyz", emptyList(), emptyList(), emptyList()))
            ),
            testSubject("{ abc def,xyz }")
        )
    }

    @Test
    fun alias() {
        fun testSubject(str: String): String? = graphQlparser.alias.parse(str).match

        assertEquals("abc", testSubject("abc:"))
        assertEquals("abc", testSubject("abc :"))
        assertNull(testSubject("abc"))
        assertNull(testSubject("123"))
    }

    @Test
    fun field() {
        fun testSubject(str: String): Field? = graphQlparser.field.parse(str).match

        assertEquals(
            Field(null, "named", emptyList(), emptyList(), emptyList()),
            testSubject("named")
        )
        assertEquals(
            Field("aliased", "named", emptyList(), emptyList(), emptyList()),
            testSubject("aliased:named")
        )
        assertEquals(
            Field(null, "named", listOf(Argument("with", Value.ValueInt("123"))), emptyList(), emptyList()),
            testSubject("named(with:123)")
        )
        assertEquals(
            Field(null, "named", emptyList(), listOf(Directive("annotated", emptyList())), emptyList()),
            testSubject("named@annotated")
        )
        assertEquals(
            Field(null, "named", emptyList(), listOf(Directive("annotated", listOf(Argument("with", Value.ValueInt("123"))))), emptyList()),
            testSubject("named@annotated(with:123)")
        )
        assertEquals(
            Field("alias", "named", listOf(Argument("with", Value.ValueInt("123"))), listOf(Directive("annotated", listOf(Argument("with", Value.ValueInt("456"))))), listOf(SelectionField(Field(null, "also", emptyList(), emptyList(), emptyList())))),
            testSubject("alias:named(with:123)@annotated(with:456){ also }")
        )
        assertEquals(
            Field("alias", "named", listOf(Argument("with", Value.ValueInt("123"))), listOf(Directive("annotated", listOf(Argument("with", Value.ValueInt("456"))))), listOf(SelectionField(Field(null, "also", emptyList(), emptyList(), emptyList())))),
            testSubject("alias : named ( with : 123 ) @annotated ( with: 456 ) { also }")
        )
    }

    @Test
    fun fragmentName() {
        fun testSubject(str: String): String? = graphQlparser.fragmentName.parse(str).match

        assertEquals("named", testSubject("named"))
        assertEquals("Named", testSubject("Named"))
        assertNull(testSubject("on"))
    }

    @Test
    fun fragmentSpread() {
        fun testSubject(str: String): FragmentSpread? = graphQlparser.fragmentSpread.parse(str).match

        assertEquals(FragmentSpread("named", emptyList()), testSubject("...named"))
        assertEquals(FragmentSpread("named", listOf(Directive("annotated", emptyList()))), testSubject("...named@annotated"))
        assertEquals(FragmentSpread("Named", listOf(Directive("annotated", emptyList()))), testSubject("... Named @annotated"))
        assertNull(testSubject("..."))
        assertNull(testSubject("... 123"))
    }

    @Test
    fun typeCondition() {
        fun testSubject(str: String): TypeCondition? = graphQlparser.typeCondition.parse(str).match

        assertEquals(TypeCondition("named"), testSubject("on named"))
        assertEquals(TypeCondition("Named"), testSubject("on Named"))
        assertNull(testSubject("on"))
        assertNull(testSubject("abc"))
        assertNull(testSubject("onnamed"))
    }

    @Test
    fun fragmentDefinition() {
        fun testSubject(str: String): FragmentDefinition? = graphQlparser.fragmentDefinition.parse(str).match

        assertEquals(
            FragmentDefinition("named", TypeCondition("typename"), emptyList(), emptyList()),
            testSubject("fragment named on typename {}")
        )
        assertEquals(
            FragmentDefinition("named", TypeCondition("typename"), emptyList(), emptyList()),
            testSubject("fragment named on typename{}")
        )
        assertEquals(
            FragmentDefinition("named", TypeCondition("typename"), listOf(Directive("annotated", emptyList())), emptyList()),
            testSubject("fragment named on typename @annotated {}")
        )
        assertEquals(
            FragmentDefinition("named", TypeCondition("typename"), listOf(Directive("annotated", emptyList())), emptyList()),
            testSubject("fragment named on typename@annotated{}")
        )
        assertNull(testSubject("fragmentnamed on typename{}"))
        assertNull(testSubject("fragment namedon typename{}"))
        assertNull(testSubject("fragment named ontypename{}"))
    }

    @Test
    fun inlineFragment() {
        fun testSubject(str: String): InlineFragment? = graphQlparser.inlineFragment.parse(str).match

        assertEquals(InlineFragment(null, emptyList(), emptyList()), testSubject("...{}"))
        assertEquals(InlineFragment(TypeCondition("named"), emptyList(), emptyList()), testSubject("...on named{}"))
        assertEquals(InlineFragment(null, listOf(Directive("annotated", emptyList())), emptyList()), testSubject("...@annotated{}"))
        assertEquals(InlineFragment(TypeCondition("named"), listOf(Directive("annotated", emptyList())), emptyList()), testSubject("... on named @annotated { }"))
    }

    @Test
    fun operationType() {
        fun testSubject(str: String): OperationType? = graphQlparser.operationType.parse(str).match

        assertEquals(OperationType.QUERY, testSubject("query"))
        assertEquals(OperationType.MUTATION, testSubject("mutation"))
        assertEquals(OperationType.SUBSCRIPTION, testSubject("subscription"))
        assertNull(testSubject("a"))
        assertNull(testSubject("1"))
        assertNull(testSubject(" "))
    }

    @Test
    fun operationDefinition() {
        fun testSubject(str: String): OperationDefinition? = graphQlparser.operationDefinition.parse(str).match

        assertEquals(OperationDefinitionSelectionSet(emptyList()), testSubject("{}"))
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.QUERY, null, emptyList(), emptyList(), emptyList())),
            testSubject("query")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.QUERY, "named", emptyList(), emptyList(), emptyList())),
            testSubject("query named")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.QUERY, null, listOf(VariableDefinition("abc", "Int", null), VariableDefinition("xyz", "Int", null)), emptyList(), emptyList())),
            testSubject("query (\$abc:Int, \$xyz:Int)")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.QUERY, null, emptyList(), listOf(Directive("annotated", emptyList()), Directive("with", emptyList())), emptyList())),
            testSubject("query @annotated @with")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.QUERY, null, emptyList(), emptyList(), emptyList())),
            testSubject("query {}")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.QUERY, "named", listOf(VariableDefinition("abc", "Int", null)), listOf(Directive("annotated", emptyList())), emptyList())),
            testSubject("query named (\$abc: Int) @annotated {}")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.MUTATION, "named", listOf(VariableDefinition("abc", "Int", null)), listOf(Directive("annotated", emptyList())), emptyList())),
            testSubject("mutation named (\$abc: Int) @annotated {}")
        )
        assertEquals(
            OperationDefinitionOperation(Operation(OperationType.SUBSCRIPTION, "named", listOf(VariableDefinition("abc", "Int", null)), listOf(Directive("annotated", emptyList())), emptyList())),
            testSubject("subscription named (\$abc: Int) @annotated {}")
        )
        assertNull(testSubject("invalid named (\$abc: Int) @annotated {}"))
    }

    @Test
    fun executableDefinition() {
        fun testSubject(str: String): ExecutableDefinition? = graphQlparser.executableDefinition.parse(str).match

        assertEquals(
            ExecutableDefinitionOperation(OperationDefinitionSelectionSet(emptyList())),
            testSubject("{}")
        )
        assertEquals(
            ExecutableDefinitionOperation(OperationDefinitionOperation(Operation(OperationType.QUERY, "named", listOf(VariableDefinition("abc", "Int", null)), listOf(Directive("annotated", emptyList())), emptyList()))),
            testSubject("query named (\$abc: Int) @annotated {}")
        )
        assertEquals(
            ExecutableDefinitionFragment(FragmentDefinition("named", TypeCondition("typename"), listOf(Directive("annotated", emptyList())), emptyList())),
            testSubject("fragment named on typename @annotated {}")
        )
    }

    @Test
    fun document() {
        fun testSubject(str: String): Document? = graphQlparser.document.parse(str).match

        assertEquals(
            Document(
                listOf(
                    DefinitionExecutable(ExecutableDefinitionOperation(OperationDefinitionOperation(Operation(OperationType.QUERY, "named", listOf(VariableDefinition("abc", "Int", null)), listOf(Directive("annotated", emptyList())), emptyList())))),
                    DefinitionExecutable(ExecutableDefinitionFragment(FragmentDefinition("named", TypeCondition("typename"), listOf(Directive("annotated", emptyList())), emptyList())))
                )
            ),
            testSubject("query named (\$abc: Int) @annotated {} \n fragment named on typename @annotated {}")
        )
    }
}
