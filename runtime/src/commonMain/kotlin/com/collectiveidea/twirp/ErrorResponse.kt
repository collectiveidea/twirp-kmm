package com.collectiveidea.twirp

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ErrorResponse(
    val code: ErrorCode = ErrorCode.Unknown,
    val msg: String,
    val meta: Map<String, @Serializable(with = AnySerializer::class) Any?>? = null
)

// See: https://github.com/twitchtv/twirp/blob/main/docs/spec_v7.md#error-codes
@Serializable
enum class ErrorCode(val statusCode: Int) {
    @SerialName("canceled")
    Canceled(HttpStatusCode.RequestTimeout.value),

    @SerialName("unknown")
    Unknown(HttpStatusCode.InternalServerError.value),

    @SerialName("invalid_argument")
    InvalidArgument(HttpStatusCode.BadRequest.value),

    @SerialName("malformed")
    Malformed(HttpStatusCode.BadRequest.value),

    @SerialName("deadline_exceeded")
    DeadlineExceeded(HttpStatusCode.RequestTimeout.value),

    @SerialName("not_found")
    NotFound(HttpStatusCode.NotFound.value),

    @SerialName("bad_route")
    BadRoute(HttpStatusCode.NotFound.value),

    @SerialName("already_exists")
    AlreadyExists(HttpStatusCode.Conflict.value),

    @SerialName("permission_denied")
    PermissionDenied(HttpStatusCode.Forbidden.value),

    @SerialName("unauthenticated")
    Unauthenticated(HttpStatusCode.Unauthorized.value),

    @SerialName("resource_exhausted")
    ResourceExhausted(HttpStatusCode.TooManyRequests.value),

    @SerialName("failed_precondition")
    FailedPrecondition(HttpStatusCode.PreconditionFailed.value),

    @SerialName("aborted")
    Aborted(HttpStatusCode.Conflict.value),

    @SerialName("out_of_range")
    OutOfRange(HttpStatusCode.BadRequest.value),

    @SerialName("unimplemented")
    Unimplemented(HttpStatusCode.NotImplemented.value),

    @SerialName("internal")
    Internal(HttpStatusCode.InternalServerError.value),

    @SerialName("unavailable")
    Unavailable(HttpStatusCode.ServiceUnavailable.value),

    @SerialName("dataloss")
    Dataloss(HttpStatusCode.InternalServerError.value)
}

// `Any?` does not appear to be supported out of the box in kotlinx-serialization. With
// a little help from https://github.com/Kotlin/kotlinx.serialization/issues/746 and
// https://github.com/Kotlin/kotlinx.serialization/issues/296 we can write a custom
// `KSerializer` to do the work for us.

//
// Serialize
//

fun Any?.toJsonElement(): JsonElement = when (this) {
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Map<*, *> -> this.toJsonObject()
    is Collection<*> -> this.toJsonArray()
    is JsonElement -> this
    else -> JsonNull
}

fun Collection<*>.toJsonArray() = JsonArray(map { it.toJsonElement() })
fun Map<*, *>.toJsonObject() =
    JsonObject(mapKeys { it.key.toString() }.mapValues { it.value.toJsonElement() })

//
// Deserialize
//

private fun JsonPrimitive.toAnyValue(): Any? {
    val content = this.content
    if (this.isString) {
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
    val intValue = content.toIntOrNull()
    if (intValue != null) {
        return intValue
    }
    val longValue = content.toLongOrNull()
    if (longValue != null) {
        return longValue
    }
    val doubleValue = content.toDoubleOrNull()
    if (doubleValue != null) {
        return doubleValue
    }
    throw Exception("Cannot convert JSON $content to value")
}

private fun JsonElement.toAnyOrNull(): Any? {
    return when (this) {
        is JsonNull -> null
        is JsonPrimitive -> toAnyValue()
        is JsonObject -> this.map { it.key to it.value.toAnyOrNull() }.toMap()
        is JsonArray -> this.map { it.toAnyOrNull() }
    }
}

object AnySerializer : KSerializer<Any?> {
    private val delegateSerializer = JsonElement.serializer()
    override val descriptor = delegateSerializer.descriptor
    override fun serialize(encoder: Encoder, value: Any?) {
        encoder.encodeSerializableValue(delegateSerializer, value.toJsonElement())
    }

    override fun deserialize(decoder: Decoder): Any? {
        val jsonElement = decoder.decodeSerializableValue(delegateSerializer)
        return jsonElement.toAnyOrNull()
    }
}
