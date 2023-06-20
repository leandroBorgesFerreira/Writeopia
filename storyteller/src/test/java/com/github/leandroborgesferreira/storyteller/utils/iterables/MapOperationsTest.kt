package com.github.leandroborgesferreira.storyteller.utils.iterables

import org.junit.Assert.assertEquals
import org.junit.Test

class MapOperationsTest {

    @Test
    fun `it should be possible to merge two simple maps`() {
        val input1 = mapOf(0 to "0")
        val input2 = mapOf(0 to "00")

        val result = MapOperations.mergeSortedMaps(input1, input2)

        assertEquals(mapOf(0 to "00", 1 to "0"), result)
    }

    @Test
    fun `it should be possible to merge two consecutive maps`() {
        val input1 = mapOf(
            0 to "0",
            1 to "1",
            2 to "2",
            3 to "3",
        )
        val input2 = mapOf(
            4 to "4",
            5 to "5",
            6 to "6",
            7 to "7",
        )

        val result = MapOperations.mergeSortedMaps(input1, input2)

        assertEquals(
            mapOf(
                0 to "0",
                1 to "1",
                2 to "2",
                3 to "3",
                4 to "4",
                5 to "5",
                6 to "6",
                7 to "7",
            ), result
        )
    }

    @Test
    fun `it should be possible to merge two complex maps`() {
        val input1 = mapOf(
            0 to "0",
            1 to "1",
            2 to "2",
            3 to "3",
        )

        val input2 = mapOf(
            0 to "00",
            2 to "22",
            3 to "33",
            5 to "5",
            7 to "7",
        )

        val result = MapOperations.mergeSortedMaps(input1, input2)

        assertEquals(
            mapOf(
                0 to "00",
                1 to "0",
                2 to "1",
                3 to "22",
                4 to "2",
                5 to "33",
                6 to "3",
                7 to "5",
                8 to "7",
            ), result
        )
    }
}