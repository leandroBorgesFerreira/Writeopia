package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState

/**
 * Class responsible to handle move requests of Stories. This class handles the logic to move a
 * Story to another position, when a Story is grouped together with another one and when a
 * Story is separated from a group.
 */
class MovementHandler {

    fun merge(stories: Map<Int, StoryStep>, info: Action.Merge): Map<Int, List<StoryStep>> {
        val sender = info.sender
        val receiver = info.receiver
        val positionTo = info.positionTo
        val positionFrom = info.positionFrom

        //This state should be allowed
        if (info.positionFrom == info.positionTo) return stories.toEditState()

        val mutableHistory = stories.toEditState()
        val receiverStepList = mutableHistory[positionTo]
        receiverStepList?.plus(sender.copy(parentId = receiver.parentId))?.let { newList ->
            mutableHistory[positionTo] = newList
        }

        if (sender.parentId == null) {
            mutableHistory.remove(positionFrom)
        } else {
            val fromGroup = mutableHistory[positionFrom]?.first()
            val newList =
                fromGroup?.steps?.filter { storyUnit -> storyUnit.localId != sender.localId }

            if (newList != null) {
                mutableHistory[positionFrom] = listOf(fromGroup.copy(steps = newList))
            }
        }

        return mutableHistory
    }

    fun move(stories: Map<Int, StoryStep>, moveInfo: Action.Move): Map<Int, StoryStep> {
        val mutable = stories.toMutableMap()
        if (mutable[moveInfo.positionTo]?.type != "space") throw IllegalStateException(
            "You can only move a story to an empty space"
        )

        return mutable[moveInfo.positionFrom]?.let { moveStory ->
            mutable[moveInfo.positionTo] = moveStory.copy(parentId = null)

            if (moveInfo.storyStep.parentId == null) {
                mutable.remove(moveInfo.positionFrom)
            } else {
                val fromGroup = mutable[moveInfo.positionFrom]
                val newList = fromGroup?.steps?.filter { storyUnit ->
                    storyUnit.localId != moveInfo.storyStep.localId
                }

                if (newList != null) {
                    mutable[moveInfo.positionFrom] = fromGroup.copy(steps = newList)
                }
            }

            mutable
        } ?: stories
    }
}