package com.github.leandroborgesferreira.storyteller.utils.iterables

import com.github.leandroborgesferreira.storyteller.utils.StoryStepFactory
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition

object MapOperations {

    //Todo: Add unit tests
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

    fun <T> addElementInPosition(
        originalMap: Map<Int, T>,
        element: T,
        addInBetween: T,
        position: Int)
    : Map<Int, T> {
        val mutable = originalMap.values.toMutableList()

        mutable.add(position, addInBetween)
        mutable.add(position, element)

        return mutable.associateWithPosition()
    }
}