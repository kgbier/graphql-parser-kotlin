package com.kgbier.graphql.modelreader

import com.kgbier.graphql.parser.GraphQLParser
import com.kgbier.graphql.parser.structure.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

object ModelReader {

    fun readModel(model: KClass<*>): Result<ModelRoot> {
        val responseSet = model.declaredMemberProperties.map { it.name }.toSet()
        val validResponseSet = setOf("data", "error")
        val difference = validResponseSet.minus(responseSet)

        if (difference != setOf("error") && difference.isNotEmpty()) {
            return Result.Failure(Throwable("Request object must expect \"data\", and optionally \"error\". Found instead $difference"))
        }

        val dataReflection = model.declaredMemberProperties.first { it.name == "data" }

        fun getSelectionTree(target: KProperty<*>): Field {
            val typeArguments = target.returnType.arguments
            val targetType = when {
                typeArguments.isEmpty() -> target.returnType.classifier as KClass<*>
                typeArguments.size == 1 -> typeArguments.first().type!!.classifier as KClass<*>
                else -> throw Throwable("Too many type arguments, panicking.")
            }
            val isPrimitive = targetType.javaPrimitiveType != null || targetType.java.name == "java.lang.String"
            if (isPrimitive) {
                return Field.Selection(target.name)
            }
            if (targetType.isSealed) {
                val sealedSubclases = targetType.sealedSubclasses
                val fields = sealedSubclases.mapIndexed { index, subclass ->
                    Field.TaggedSelection(sealedSubclases[index].simpleName!!, subclass.declaredMemberProperties.map(::getSelectionTree).toSet())
                }
                return Field.SelectionSet(target.name, fields.toSet())
            }
            return Field.SelectionSet(target.name, targetType.declaredMemberProperties.map(::getSelectionTree).toSet())
        }

        val tree = getSelectionTree(dataReflection)
        return if (tree is Field.SelectionSet && tree.name == "data") {
            Result.Success(ModelRoot(tree.fields))
        } else {
            Result.Failure(Throwable("Unexpected state, panicking."))
        }
    }

    fun readQuery(string: String): Result<QueryRoot> {
        val document = GraphQLParser.parse(string) ?: return Result.Failure(Throwable("GraphQL Query is not valid"))

        val fragments = mutableMapOf<String, List<Selection>>()
        val operations = mutableListOf<OperationDefinition>()

        document.definitions.forEach {
            when (it) {
                is DefinitionExecutable -> when (val executableDefinition = it.definition) {
                    is ExecutableDefinitionFragment ->
                        fragments[executableDefinition.definition.name] = executableDefinition.definition.selectionSet
                    is ExecutableDefinitionOperation -> operations.add(executableDefinition.definition)
                }
            }
        }

        if (operations.size == 0) {
            return Result.Failure(Throwable("One operation required (mutation, query, or subscription)"))
        }

        val operation = if (operations.size == 1) {
            operations.first()
        } else {
            return Result.Failure(Throwable("Multiple operations are not supported"))
        }

        val rootSelectionSet = when (operation) {
            is OperationDefinitionOperation -> operation.definition.selectionSet
            is OperationDefinitionSelectionSet -> operation.selectionSet
        }

        fun getSelectionTree(target: Selection): Field = when (target) {
            is SelectionField -> if (target.selection.selectionSet.isEmpty()) {
                Field.Selection(target.selection.name)
            } else {
                Field.SelectionSet(target.selection.name, target.selection.selectionSet.map(::getSelectionTree).toSet())
            }
            is SelectionFragmentSpread -> Field.Spread(fragments[target.selection.name]!!.map(::getSelectionTree).toSet())
            is SelectionInlineFragment -> if (target.selection.typeCondition == null) {
                Field.Spread(target.selection.selectionSet.map(::getSelectionTree).toSet())
            } else {
                Field.TaggedSelection(target.selection.typeCondition.namedType, target.selection.selectionSet.map(::getSelectionTree).toSet())
            }
        }

        val root = QueryRoot(rootSelectionSet.map(::getSelectionTree).toSet())

        return Result.Success(root)
    }
}