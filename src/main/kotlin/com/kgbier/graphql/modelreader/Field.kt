package com.kgbier.graphql.modelreader

data class ModelRoot(val fields: Set<Field>)
data class QueryRoot(val fields: Set<Field>)

sealed class Field {
    data class Spread(val fields: Set<Field>) : Field()
    data class Selection(val name: String) : Field()
    data class SelectionSet(val name: String, val fields: Set<Field>) : Field()
    data class TaggedSelection(val tag: String, val fields: Set<Field>) : Field()
}