package com.github.leandroborgesferreira.storyteller.utils.iterables

object MapOperations {

    fun <T> mergeSortedMaps(originalMap: Map<Int, T>, newMap: Map<Int, T>): Map<Int, T> {
        val mutable = originalMap.mapKeys { (key, _) -> key * 10 }.toMutableMap()

        newMap.mapKeys { (key, _) -> key * 10 }.forEach { (key, value) ->
            if (mutable[key] != null) {
                mutable[key - 1] = value
            } else {
                mutable[key] = value
            }
        }

        var acc = 0
        return mutable.toSortedMap().mapKeys { acc++ }
    }
}