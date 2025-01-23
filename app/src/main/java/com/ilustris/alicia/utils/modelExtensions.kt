package com.ilustris.alicia.utils

import android.util.Log
import com.google.gson.Gson
import java.util.regex.Pattern

inline fun <reified T> mapExtraToData(data: String?): T? {
    val mappedResponse =
        data
            ?.replace("<", "{")
            ?.replace(">", "}")
    return Gson().fromJson(mappedResponse, T::class.java)
}

inline fun <reified T> parseJson(json: String): T? =
    try {
        val formattedJson = json.removeSlashes()
        Log.d(T::class.java.simpleName, "parseJson: Parsing $formattedJson")
        Gson().fromJson(formattedJson, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

fun parseJson(
    json: String,
    clazz: Class<*>,
): Any? =
    try {
        val formattedJson = json.removeSlashes()
        Log.d(clazz.simpleName, "parseJson: Parsing $formattedJson")
        Gson().fromJson(formattedJson, clazz)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

fun <T> toJsonSchema(
    clazz: Class<T>,
    specificReplacement: Pair<String, String>? = null,
): String =
    "\n{" +
        "\n${joinDeclaredFields(clazz, specificReplacement)}" +
        "\n}"

fun String.removePackagePrefix(): String =
    this
        .substringAfterLast(".")
        .replace(".", "")

inline fun <reified T> toJsonSchema(): String =
    "\n\"{" +
        "   \n${joinDeclaredFields<T>()}" +
        "\n}\""

inline fun <reified T> joinDeclaredFields(): String =
    T::class
        .constructors
        .first()
        .parameters
        .filter {
            it.name != "\$stable"
        }.joinToString(separator = ",\n") {
            "\"${it.name}\": ${it.type.toString().removePackagePrefix()}"
        }

fun joinDeclaredFields(
    clazz: Class<*>,
    replaceSpecifFieldType: Pair<String, String>? = null,
): String =
    clazz
        .declaredFields
        .filter {
            it.name != "\$stable"
        }.joinToString(separator = ",\n") {
            if (replaceSpecifFieldType != null && it.name == replaceSpecifFieldType.first) {
                "\"${replaceSpecifFieldType.first}\": \"${replaceSpecifFieldType.second}\""
            } else {
                "\"${it.name}\": \"${it.type.toString().removePackagePrefix()}\""
            }
        }

fun String.getObjectFromField(field: String): String {
    val fieldIndex = this.indexOf(field)
    val delimiter = this.indexOf("}", fieldIndex)
    return this.substring(fieldIndex, delimiter)
}

fun String.getField(field: String): Triple<String, Int, Int> {
    Log.w("modelExtensions", "getField: Looking for $field in\n$this")
    if (!contains(field)) {
        return Triple(this, 0, 0)
    }
    val fieldIndex = this.indexOf(field)
    val delimiter = getDelimiterIndex(this, field)
    val commaIndex = this.indexOf(":", fieldIndex) + 1
    val field = this.substring(fieldIndex, delimiter)
    Log.i("modelExtension", "field Value =>\n$field\n$commaIndex,$delimiter")
    return Triple(field, commaIndex, delimiter)
}

fun getDelimiterIndex(
    data: String,
    field: String,
): Int {
    val fieldIndex = data.indexOf(field)
    val fieldData = data.substring(fieldIndex)
    if (fieldData.contains(",")) {
        return data.indexOf(",", fieldIndex)
    } else if (fieldData.contains("}")) {
        return data.indexOf("}", fieldIndex)
    }
    return data.indexOf(",", fieldIndex)
}

fun String.containsNull() = contains("null")

fun String.replaceNullOnField(
    field: String,
    newValue: String,
): String {
    val fieldData = this.getField(field)
    if (fieldData.first.containsNull()) {
        return this.replaceRange(fieldData.second, fieldData.third, newValue)
    }
    return this
}

fun String.filterJsonString(): String {
    Log.i("modelExtensions", "filterJsonString: Filtering Json String =>\n$this")
    val doubleSlash = "$SLASH$SLASH"
    val fieldRegex = "\"(?:[^\\\\\"]|\\\\\\\\|\\\\\")*\":"
    val valueRegex = ""
    val jsonRegex = "$fieldRegex ([0-9]|)+"
    val charsAndDigits = "[A-Za-z0-9]"
    val pattern = Pattern.compile("(?<=\")(^\"\\]|\\.)*?(?=\":,)")
    val matcher = pattern.matcher(this)
    val jsonString = StringBuilder("{")
    var firstField = true
    while (matcher.find()) {
        if (!firstField) {
            jsonString.append(",")
        } else {
            Log.i("modelExtension", "filterJsonString: Matcher results =>\n${matcher.group()}")
        }
        firstField = false
        jsonString.append("\"${matcher.group()}\":")
        // Extract the value for the field
        val valueStart = matcher.end() + 2
        val valueEnd = jsonString.indexOf('"', valueStart)
        jsonString.append(jsonString.substring(valueStart, valueEnd))
    }

    jsonString.append("}")

    Log.i("modelExtenstion", "filterJsonString: Json Formatted =>\n$jsonString")
    return jsonString.toString()
}
