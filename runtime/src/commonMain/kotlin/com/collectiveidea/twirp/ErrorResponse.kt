package com.collectiveidea.twirp

import com.collectiveidea.serialization.json.AnySerializer
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ErrorResponse(
    val code: ErrorCode = ErrorCode.Unknown,
    val msg: String,
    @Suppress("ktlint:standard:annotation")
    val meta: Map<String, @Serializable(with = AnySerializer::class) Any?>? = null,
)

// See: https://github.com/twitchtv/twirp/blob/main/docs/spec_v7.md#error-codes
@Serializable
public enum class ErrorCode(
    public val statusCode: Int,
) {
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
    Dataloss(HttpStatusCode.InternalServerError.value),
}
