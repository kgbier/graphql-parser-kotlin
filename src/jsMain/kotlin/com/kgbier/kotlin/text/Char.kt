package com.kgbier.kotlin.text

val digitRange = '0'..'9'
val letterLowercaseRange = 'a'..'z'
val letterUppercaseRange = 'A'..'Z'

@Suppress("NOTHING_TO_INLINE")
actual inline fun Char.isLetterOrDigit(): Boolean = this in letterUppercaseRange || this in letterLowercaseRange || this in digitRange

@Suppress("NOTHING_TO_INLINE")
actual inline fun Char.isDigit(): Boolean = this in digitRange
