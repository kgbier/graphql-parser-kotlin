package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.Document
import com.kgbier.graphql.parser.substring.Substring

data class GraphQlParseResult(val match: Document?, val rest: Substring)

object GraphQLParser {

    private val parser = GraphQl()

    internal fun parseWithResult(str: String): GraphQlParseResult {
        val result = parser.document.parse(str)
        return GraphQlParseResult(result.match, result.rest)
    }

    fun parse(str: String): Document? = parseWithResult(str).match
}
