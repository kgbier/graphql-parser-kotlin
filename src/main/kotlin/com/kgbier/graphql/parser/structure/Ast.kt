package com.kgbier.graphql.parser.structure

data class Document(
        val definitions: List<Definition>
)

sealed class Definition {
    data class DefinitionExecutable(val definition: ExecutableDefinition) : Definition()
}

sealed class ExecutableDefinition {
    data class ExecutableDefinitionOperation(val definition: OperationDefinition) : ExecutableDefinition()
    data class ExecutableDefinitionFragment(val definition: FragmentDefinition) : ExecutableDefinition()
}

sealed class OperationDefinition {
    data class OperationDefinitionOperation(val definition: Operation) : OperationDefinition()
    data class OperationDefinitionSelectionSet(val selectionSet: List<Selection>) : OperationDefinition()

    data class Operation(
            val operationType: OperationType,
            val name: String?,
            val variableDefinitions: List<VariableDefinition>,
            val directives: List<Directive>,
            val selectionSet: List<Selection>
    )
}

data class FragmentDefinition(
        val name: String,
        val typeCondition: TypeCondition,
        val directives: List<Directive>,
        val selectionSet: List<Selection>
)

data class InlineFragment(
        val typeCondition: TypeCondition?,
        val directives: List<Directive>,
        val selectionSet: List<Selection>
)

data class FragmentSpread(
        val name: String,
        val directives: List<Directive>
)

enum class OperationType {
    QUERY,
    MUTATION,
    SUBSCRIPTION
}

data class TypeCondition(val namedType: String)

data class Field(
        val alias: String?,
        val name: String,
        val arguments: List<Argument>,
        val directives: List<Directive>,
        val selectionSet: List<Selection>
)

sealed class Selection {
    data class SelectionField(val selection: Field) : Selection()
    data class SelectionFragmentSpread(val selection: FragmentSpread) : Selection()
    data class SelectionInlineFragment(val selection: InlineFragment) : Selection()
}

data class Directive(
        val name: String,
        val arguments: List<Argument>
)

data class Argument(
        val name: String,
        val value: Value
)

data class VariableDefinition(
        val variable: String,
        val type: String,
        val defaultValue: Value?
)

sealed class Value {
    data class ValueVariable(val name: String) : Value()
    data class ValueInt(val value: String) : Value()
    data class ValueFloat(val value: String) : Value()
    data class ValueString(val value: String) : Value()
    data class ValueBoolean(val value: Boolean) : Value()
    object ValueNull : Value()
    data class ValueEnum(val value: String) : Value()
    data class ValueList(val value: List<Value>) : Value()
    data class ValueObject(val value: List<ObjectField>) : Value()
}

data class ObjectField(val name: String, val value: Value)