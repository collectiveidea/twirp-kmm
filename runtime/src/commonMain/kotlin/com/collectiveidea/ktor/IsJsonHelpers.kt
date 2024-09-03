package com.collectiveidea.ktor

import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal fun ContentType.isJson(): Boolean = match(ContentType.Application.Json)

internal fun HttpResponse.isJson(): Boolean = contentType()?.isJson() == true
