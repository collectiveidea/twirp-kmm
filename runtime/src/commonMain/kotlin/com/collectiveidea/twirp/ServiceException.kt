package com.collectiveidea.twirp

import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode

public class ServiceException(
    public val error: ErrorResponse,
    public val responseException: ResponseException,
) : RuntimeException(error.msg, responseException) {
    public fun isNotModifiedException(): Boolean =
        responseException is RedirectResponseException && responseException.response.status == HttpStatusCode.NotModified
}
