package com.collectiveidea.ktor

import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

fun ContentType.isJson() = match(ContentType.Application.Json)

fun HttpResponse.isJson() = contentType()?.isJson() == true
