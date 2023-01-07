package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.Document
import com.kgbier.graphql.parser.substring.Substring
import kotlin.js.JsExport

data class GraphQlParseResult(val match: Document?, val rest: Substring)

@JsExport
object GraphQLParser {

    private val parser = GraphQl()

    internal fun parseWithResult(str: String): GraphQlParseResult {
        val result = parser.document.parse(str.trim())
        return GraphQlParseResult(result.match, result.remainder)
    }

    fun parse(str: String): Document? = parseWithResult(str).match
}
