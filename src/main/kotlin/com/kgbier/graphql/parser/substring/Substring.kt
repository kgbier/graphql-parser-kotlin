package com.kgbier.graphql.parser.substring

class Substring(string: String) : CharSequence {
    private val backingString = string
    private var range: IntRange = 0..string.length

    var state: IntRange
        get() = range
        set(value) {
            range = value
        }

    fun advance(characters: Int = 1) {
        range = (range.first + characters)..range.last
    }

    override val length: Int
        get() = range.count()

    override fun get(index: Int): Char = backingString[index + range.first]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
            backingString.subSequence(startIndex + range.first, endIndex + range.first)


    override fun toString(): String = backingString.substring(range.first, range.last)
}
