package com.github.leandroborgesferreira.storyteller.utils.iterables

object MapOperations {

    fun <T> mergeSortedMaps(originalMap: Map<Int, T>, newMap: Map<Int, T>): Map<Int, T> {
        return originalMap.toMutableMap().apply { putAll(newMap) }
    }
}