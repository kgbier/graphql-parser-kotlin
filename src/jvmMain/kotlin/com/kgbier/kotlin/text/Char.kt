package com.kgbier.kotlin.text

import kotlin.text.isDigit as ktIsDigit
import kotlin.text.isLetterOrDigit as ktIsLetterOrDigit

@Suppress("NOTHING_TO_INLINE")
actual inline fun Char.isLetterOrDigit(): Boolean = ktIsLetterOrDigit()

@Suppress("NOTHING_TO_INLINE")
actual inline fun Char.isDigit(): Boolean = ktIsDigit()
