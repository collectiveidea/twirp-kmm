package com.collectiveidea.ktor

import io.ktor.http.ContentType
import io.ktor.http.withCharset
import io.ktor.utils.io.charsets.Charsets
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsJsonHelpersKtTest {
    @Test
    fun `ContentType isJson returns true when missing charset`() {
        val contentType = ContentType.Application.Json
        assertTrue(contentType.isJson())
    }

    @Test
    fun `ContentType isJson returns true when charset present`() {
        val contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
        assertTrue(contentType.isJson())
    }

    @Test
    fun `ContentType isJson returns false when not explicitly json`() {
        val contentType = ContentType.Application.Any
        assertFalse(contentType.isJson())
    }
}
