package com.collectiveidea.twirp

import com.collectiveidea.ktor.isJson
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val twirpErrorJsonDecoder: Json by lazy {
    // Force unexpected error string value to map to `ErrorCode.Unknown` rather
    // than throw a runtime exception decoding.
    Json { coerceInputValues = true }
}

/**
 * @param baseUrl The base URL of the root Twirp service, excluding the protobuf service name, and
 *  with a trailing slash: e.g. `https://www.example.com/twirp/`
 */
public fun HttpClientConfig<*>.installTwirp(baseUrl: String) {
    expectSuccess = true

    defaultRequest {
        url(baseUrl)

        accept(ContentType.Application.ProtoBuf)
        contentType(ContentType.Application.ProtoBuf)
    }

    // Intercept exceptions to extract JSON message content before passing the exceptions along
    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, _ ->
            // Re-throw anything unexpected to prevent swallowing errors
            if (exception !is ResponseException) {
                throw exception
            }

            val responseBody: String = exception.response.bodyAsText()
            val error: ErrorResponse = if (exception.response.isJson()) {
                twirpErrorJsonDecoder.decodeFromString(responseBody)
            } else {
                ErrorResponse(ErrorCode.Unknown, responseBody, null)
            }

            throw ServiceException(error, exception)
        }
    }
}
