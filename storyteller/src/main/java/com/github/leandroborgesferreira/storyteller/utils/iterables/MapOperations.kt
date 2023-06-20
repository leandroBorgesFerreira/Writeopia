package com.github.leandroborgesferreira.storyteller.utils.iterables

object MapOperations {

    fun <T> mergeSortedMaps(
        originalMap: Map<Int, T>,
        newMap: Map<Int, T>,
        addInBetween: () -> T
    ): Map<Int, T> {
        val mutableList = originalMap.values.toMutableList()

        newMap.toSortedMap().forEach { (position, value) ->
            mutableList.add(position, addInBetween())
            mutableList.add(position, value)
        }

        var acc = 0
        return mutableList.associateBy { acc++ }
    }
}