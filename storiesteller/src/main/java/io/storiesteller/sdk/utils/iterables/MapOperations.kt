package io.storiesteller.sdk.utils.iterables

import io.storiesteller.sdk.utils.extensions.associateWithPosition
import kotlin.math.min

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

        return mutableList.associateWithPosition()
    }

    fun <T> addElementInPosition(
        originalMap: Map<Int, T>,
        element: T,
        addInBetween: T,
        position: Int
    ): Map<Int, T> {
        val mutable = originalMap.values.toMutableList()

        mutable.add(min(position, mutable.lastIndex + 1), addInBetween)
        mutable.add(min(position, mutable.lastIndex + 1), element)

        return mutable.associateWithPosition()
    }
}