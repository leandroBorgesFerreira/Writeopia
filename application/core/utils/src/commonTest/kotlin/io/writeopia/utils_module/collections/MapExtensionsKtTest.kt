package io.writeopia.utils_module.collections

import kotlin.test.Test
import kotlin.test.assertEquals

class MapExtensionsKtTest {

    @Test
    fun mapsShouldBeMergedCorrectly() {
        val map1 = mapOf(
            "0" to listOf(1, 2, 5),
            "1" to listOf(3, 7)
        )

        val map2 = mapOf(
            "0" to listOf(3, 4),
            "2" to listOf(6, 8)
        )

        val result = map1.merge(map2)
        val expectedMap = mapOf(
            "0" to listOf(1, 2, 5, 3, 4),
            "1" to listOf(3, 7),
            "2" to listOf(6, 8)
        )

        assertEquals(expectedMap, result)
    }

    @Test
    fun nodeTreeShouldBeCorrectlyCreated() {
        
    }
}
