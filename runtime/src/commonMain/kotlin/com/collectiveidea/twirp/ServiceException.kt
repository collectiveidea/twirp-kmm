package com.collectiveidea.twirp

import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode

class ServiceException(
    val error: ErrorResponse,
    val responseException: ResponseException,
) : RuntimeException(error.msg, responseException) {
    fun isNotModifiedException(): Boolean =
        responseException is RedirectResponseException && responseException.response.status == HttpStatusCode.NotModified
}
