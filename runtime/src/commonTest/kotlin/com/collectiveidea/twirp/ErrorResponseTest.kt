package com.collectiveidea.twirp

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ErrorResponseTest {
    private fun decode(jsonString: String): ErrorResponse {
        val json = Json { coerceInputValues = true }
        return json.decodeFromString(jsonString)
    }

    @Test
    fun decodes_properly_with_missing_meta() {
        val error = decode("""{"code":"not_found","msg":"not found"}""")

        assertEquals(ErrorCode.NotFound, error.code)
        assertEquals("not found", error.msg)
        assertNull(error.meta)
    }

    @Test
    fun decodes_unrecognized_enum_as_enum() {
        val error = decode("""{"code":"this_is_not_available","msg":"new error code"}""")

        assertEquals(ErrorCode.Unknown, error.code)
        assertEquals("new error code", error.msg)
    }

    @Test
    fun decodes_meta_with_arbitrary_values() {
        val error = decode(
            """{
            "code":"canceled",
            "msg":"",
            "meta": {
                "key1": "a string",
                "key2": false,
                "key3": 51,
                "key4": [true, 1, "test", {"nestedKey0": "yup"}],
                "key5": {
                    "nestedKey1": "another string",
                    "nestedKey2": true,
                    "nestedKey3": 17,
                    "nestedKey4": [9, null]
                },
                "key6": null,
                "key7": 12.53,
                "key8": 4000000000
            }
        }
            """.trimIndent(),
        )

        val meta = error.meta
        assertNotNull(meta)
        assertEquals(
            mapOf(
                "key1" to "a string",
                "key2" to false,
                "key3" to 51,
                "key4" to listOf(
                    true,
                    1,
                    "test",
                    mapOf("nestedKey0" to "yup"),
                ),
                "key5" to mapOf(
                    "nestedKey1" to "another string",
                    "nestedKey2" to true,
                    "nestedKey3" to 17,
                    "nestedKey4" to listOf(9, null),
                ),
                "key6" to null,
                "key7" to 12.53,
                "key8" to 4000000000,
            ),
            meta,
        )
    }
}
