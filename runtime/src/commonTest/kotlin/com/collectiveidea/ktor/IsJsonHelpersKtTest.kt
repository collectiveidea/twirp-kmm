package com.collectiveidea.ktor

import io.ktor.http.ContentType
import io.ktor.http.withCharset
import io.ktor.utils.io.charsets.Charsets
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsJsonHelpersKtTest {
    @Test
    fun contentType_isJson_returns_true_when_missing_charset() {
        val contentType = ContentType.Application.Json
        assertTrue(contentType.isJson())
    }

    @Test
    fun contentType_isJson_returns_true_when_charset_present() {
        val contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
        assertTrue(contentType.isJson())
    }

    @Test
    fun contentType_isJson_returns_false_when_not_explicitly_json() {
        val contentType = ContentType.Application.Any
        assertFalse(contentType.isJson())
    }
}
