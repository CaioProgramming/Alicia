package com.ilustris.alicia.utils

fun String.addSlashToQuotes(): String {
    if (!contains("\"")) return this
    return replace("\"", "\\\"")
}

fun String.removeDuplicatedSlash(): String = replace("\\\\", "\\")

fun String.replaceSingleQuotes(): String = replace("'", "\"")

fun String.removeLineBreaks(): String = replace("\n", "")

fun String.containsCurlyBraces(): Boolean = contains("{") && contains("}")

fun String.getCurlyBracesIndexes(): Pair<Int, Int> = Pair(indexOf("{"), indexOf("}"))

fun emptyString() = ""

fun String.replaceCurlyBracesForTag() =
    replace("{", "\"<")
        .replace("}", ">\"")
        .replace("}\"", ">\"")
        .replace("\"{", "\"<")

fun String.removeSlashes(): String = replace("\\", "")

fun String.replaceSlashString(): String = replace("${SLASH}${DOUBLE_QUOTES}", DOUBLE_QUOTES)

fun String.removeDuplicatedSlashes(): String = replace("${SLASH}$SLASH", "")

fun String.removeDoubleQuotes(): String {
    if (!contains(DOUBLE_QUOTES)) return this
    return replace(DOUBLE_QUOTES, emptyString())
}

fun String.removeDuplicatedQuotes(): String = replace("${DOUBLE_QUOTES}${DOUBLE_QUOTES}", DOUBLE_QUOTES)

fun String.filterOnlyChars(): String = filter { it.isLetter() }

fun String.removeColon(): String = replace(":", "")

fun String.replaceAndStringfy(value: String): String = this.replace(value, "$DOUBLE_QUOTES$value$DOUBLE_QUOTES")

fun String.containsNumber(): Boolean = any { it.isDigit() }

fun String.removeBrackets(): String = replace("[", "").replace("]", "")

const val DOUBLE_QUOTES = "\""
const val SLASH = "\\"
