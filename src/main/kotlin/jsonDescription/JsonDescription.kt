package jsonDescription

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import stringUtil.plusAssign

sealed class JsonItemDescription

object Unsupported : JsonItemDescription()

sealed class Supported : JsonItemDescription()

data class Obj(val fields: MutableMap<String, Supported> = mutableMapOf()) : Supported()

data class Arr(val itemTypes: MutableList<Supported> = mutableListOf()) : Supported()

sealed class Scalar : Supported()

object Str : Scalar() {
    override fun toString(): String = "Str"
}

object _Int : Scalar() {
    override fun toString(): String = "Int"
}

object Float : Scalar() {
    override fun toString(): String = "Float"
}

object Bool : Scalar() {
    override fun toString(): String = "Bool"
}

fun JsonItemDescription.toJsonString() = when (this) {
    is Supported -> this.toJsonString()
    Unsupported -> "[\"Unsupported\"]"
}

fun Supported.toJsonString(): String {
    val separator = ","
    val itemDescription = this
    val res = StringBuilder()
    when (itemDescription) {
        is Obj -> {
            res += "{"
            var divider = ""
            for ((name, item) in itemDescription.fields) {
                res += divider
                res += """"$name":"""
                res += item.toJsonString()
                divider = separator
            }
            res += "}"
        }
        is Arr -> res += itemDescription.itemTypes.joinToString(separator, "[", "]") { it.toJsonString() }
        is Scalar -> res += """"$itemDescription""""
    }
    return res.toString()
}

fun readJsonItemDescription(jsonParser: JsonParser): JsonItemDescription {
    if (null == jsonParser.currentToken) {
        jsonParser.nextToken()
    }
    when (jsonParser.currentToken) {
        JsonToken.VALUE_STRING -> return Str
        JsonToken.VALUE_NUMBER_INT -> return _Int
        JsonToken.VALUE_NUMBER_FLOAT -> return Float
        JsonToken.VALUE_TRUE, JsonToken.VALUE_FALSE -> return Bool
        JsonToken.START_OBJECT -> {
            val res = Obj()
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                val fieldName = jsonParser.currentName
                jsonParser.nextToken()
                val itemDescription = readJsonItemDescription(jsonParser)
                if (itemDescription is Supported) {
                    res.fields[fieldName] = itemDescription
                }
            }
            return res
        }
        JsonToken.START_ARRAY -> {
            val res = Arr()
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                val itemDescription = readJsonItemDescription(jsonParser)
                if (itemDescription is Supported) {
                    if (res.itemTypes.notContains(itemDescription)) {
                        res.itemTypes += itemDescription
                    }
                }
            }
            return res
        }
        else -> {
            jsonParser.skipChildren()
            return Unsupported
        }
    }
}

private fun MutableList<Supported>.notContains(itemDescription: Supported): Boolean {
    when (itemDescription) {
        is Obj -> {
            for (item in this) {
                if (item is Obj && item.fields.size == itemDescription.fields.size) {
                    if (item.toJsonString() == itemDescription.toJsonString()) {
                        return false
                    }
                }
            }
        }
        is Arr -> {
            for (item in this) {
                if (item is Arr && item.itemTypes.size == itemDescription.itemTypes.size) {
                    if (item.toJsonString() == itemDescription.toJsonString()) {
                        return false
                    }
                }
            }
        }
        is Scalar -> {
            if (this.contains(itemDescription)) {
                return false
            }
        }
    }

    return true
}
