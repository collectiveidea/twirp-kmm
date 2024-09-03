package com.collectiveidea.twirp

import kotlinx.serialization.decodeFromString
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
                "key6": null
            }
        }
            """.trimIndent(),
        )

        val meta = error.meta
        assertNotNull(meta)
        assertEquals("a string", meta["key1"])
        assertEquals(false, meta["key2"])
        assertEquals(51, meta["key3"])

        val key4Array = meta["key4"] as ArrayList<*>
        assertEquals(4, key4Array.size)
        assertEquals(true, key4Array[0])
        assertEquals(1, key4Array[1])
        assertEquals("test", key4Array[2])
        val key4ArrayNestedMap = key4Array[3] as Map<*, *>
        assertEquals(1, key4ArrayNestedMap.keys.size)
        assertEquals("yup", key4ArrayNestedMap["nestedKey0"])

        val key5Map = meta["key5"] as Map<*, *>
        assertEquals("another string", key5Map["nestedKey1"])
        assertEquals(true, key5Map["nestedKey2"])
        assertEquals(17, key5Map["nestedKey3"])
        val key5NestedArray = key5Map["nestedKey4"] as ArrayList<*>
        assertEquals(2, key5NestedArray.size)
        assertEquals(9, key5NestedArray[0])
        assertEquals(null, key5NestedArray[1])

        assertEquals(null, meta["key6"])
    }
}
