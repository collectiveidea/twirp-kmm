package com.collectiveidea.serialization.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

// `Any?` does not appear to be supported out of the box in kotlinx-serialization.
//
// With a little help from https://github.com/Kotlin/kotlinx.serialization/issues/746 and
// https://github.com/Kotlin/kotlinx.serialization/issues/296 we can write a custom
// `KSerializer` to do the work for us.

internal object AnySerializer : KSerializer<Any?> {
    private val delegateSerializer = JsonElement.serializer()
    override val descriptor = delegateSerializer.descriptor

    override fun serialize(
        encoder: Encoder,
        value: Any?,
    ) {
        encoder.encodeSerializableValue(delegateSerializer, value.toJsonElement())
    }

    override fun deserialize(decoder: Decoder): Any? = decoder.decodeSerializableValue(delegateSerializer).toAnyOrNull()
}

//
// Serialize
//

internal fun Any?.toJsonElement(): JsonElement = when (this) {
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Map<*, *> -> toJsonObject()
    is Collection<*> -> toJsonArray()
    is JsonElement -> this
    else -> JsonNull
}

private fun Collection<*>.toJsonArray() = JsonArray(map { it.toJsonElement() })

private fun Map<*, *>.toJsonObject() = JsonObject(mapKeys { it.key.toString() }.mapValues { it.value.toJsonElement() })

//
// Deserialize
//

internal fun JsonElement.toAnyOrNull(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> toAnyValue()
    is JsonObject -> map { it.key to it.value.toAnyOrNull() }.toMap()
    is JsonArray -> map { it.toAnyOrNull() }
}

private fun JsonPrimitive.toAnyValue(): Any? {
    val content = this.content
    if (isString) {
        return content
    }
    if (content.equals("null", ignoreCase = true)) {
        return null
    }
    if (content.equals("true", ignoreCase = true)) {
        return true
    }
    if (content.equals("false", ignoreCase = true)) {
        return false
    }
    return content.toIntOrNull()
        ?: content.toLongOrNull()
        ?: content.toDoubleOrNull()
        ?: throw Exception("Cannot convert JSON $content to value")
}
