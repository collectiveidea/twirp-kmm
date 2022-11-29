package com.collectiveidea.twirp

import io.ktor.client.plugins.ResponseException

class ServiceException(val error: ErrorResponse, responseException: ResponseException) :
    RuntimeException(error.msg, responseException)
