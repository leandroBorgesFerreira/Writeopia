package com.github.leandroborgesferreira.storyteller.normalization.addinbetween

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition
import java.util.Stack
import java.util.UUID

/**
 * This normalizer will guarantee that a new StoryUnit is added between all items of
 * a List<StoryUnit> this normalizer can be very useful to insert spaces between a list
 * of StoryUnits.
 */
class AddSteps(
    private val addInBetween: () -> StoryStep,
    private val addAtLast: () -> StoryStep,
    private val skipFirst: Boolean = true
) {

    fun insert(unit: Map<Int, StoryStep>): Map<Int, StoryStep> =
        insert(unit.values).associateWithPosition()

    fun insert(units: Iterable<StoryStep>): List<StoryStep> {
        val stack: Stack<StoryStep> = Stack()
        val typeToAdd = addInBetween().type
        val typeAtLast = addAtLast().type

        units.forEach { storyUnit ->
            when {
                storyUnit.type == typeAtLast -> {
                    if (stack.peek().type != typeToAdd) {
                        stack.add(addInBetween())
                    }
                    stack.add(addAtLast())
                }

                stack.isEmpty() && storyUnit.type != typeToAdd -> {
                    if (!skipFirst) {
                        stack.add(addInBetween())
                    }
                    stack.add(storyUnit.copy(localId = UUID.randomUUID().toString()))
                }

                stack.isEmpty() && storyUnit.type == typeToAdd -> {
                    stack.add(storyUnit.copy(localId = UUID.randomUUID().toString()))
                }

                storyUnit.type != typeToAdd && stack.peek().type == typeToAdd -> {
                    stack.add(storyUnit.copy(localId = UUID.randomUUID().toString()))
                }

                storyUnit.type == typeToAdd && stack.peek()?.type != typeToAdd -> {
                    stack.add(storyUnit.copy(localId = UUID.randomUUID().toString()))
                }

                storyUnit.type != typeToAdd && stack.peek()?.type != typeToAdd -> {
                    stack.add(addInBetween())
                    stack.add(storyUnit.copy(localId = UUID.randomUUID().toString()))
                }

                storyUnit.type == typeToAdd && stack.peek()?.type == typeToAdd -> {}
            }
        }

        val lastElement = stack.peek()

        if (lastElement.type != typeAtLast) {
            if (lastElement.type != typeToAdd) {
                stack.add(addInBetween())
            }

            stack.add(addAtLast())
        }

        return stack.toList()
    }


    companion object {
        fun spaces(): AddSteps =
            AddSteps(
                addInBetween = {
                    StoryStep(type = StoryType.SPACE.type)
                },
                addAtLast = {
                    StoryStep(type = StoryType.LARGE_SPACE.type)
                }
            )
    }
}
