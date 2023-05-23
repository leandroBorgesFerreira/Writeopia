package com.github.leandroborgesferreira.storyteller.normalization.addinbetween

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition
import java.util.Stack
import java.util.UUID

/**
 * This normalizer will guarantee that a new StoryUnit is added between all items of
 * a List<StoryUnit> this normalizer can be very useful to insert spaces between a list
 * of StoryUnits.
 */
class AddInBetween(private val unitToAdd: () -> StoryUnit) {

    fun insert(unit: Map<Int, StoryUnit>): Map<Int, StoryUnit> =
        insert(unit.values).associateWithPosition()

    fun insert(units: Iterable<StoryUnit>): List<StoryUnit> {
        val stack: Stack<StoryUnit> = Stack()
        val typeToAdd = unitToAdd().type

        units.forEach { storyUnit ->
            when {
                stack.isEmpty() && storyUnit.type != typeToAdd -> {
                    stack.add(unitToAdd())
                    stack.add(storyUnit.copyWithNewId(UUID.randomUUID().toString()))
                }

                stack.isEmpty() && storyUnit.type == typeToAdd -> {
                    stack.add(storyUnit.copyWithNewId(UUID.randomUUID().toString()))
                }

                storyUnit.type != typeToAdd && stack.peek().type == typeToAdd -> {
                    stack.add(storyUnit.copyWithNewId(UUID.randomUUID().toString()))
                }

                storyUnit.type == typeToAdd && stack.peek()?.type != typeToAdd -> {
                    stack.add(storyUnit.copyWithNewId(UUID.randomUUID().toString()))
                }

                storyUnit.type != typeToAdd && stack.peek()?.type != typeToAdd -> {
                    stack.add(unitToAdd())
                    stack.add(storyUnit.copyWithNewId(UUID.randomUUID().toString()))
                }

                storyUnit.type == typeToAdd && stack.peek()?.type == typeToAdd -> {}
            }
        }

        if (stack.peek().type != typeToAdd) {
            stack.add(unitToAdd())
        }

        return stack.toList()
    }


    companion object {
        fun spaces(): AddInBetween =
            AddInBetween(
                unitToAdd = {
                    StoryStep(
                        localId = UUID.randomUUID().toString(),
                        type = "space",
                    )
                }
            )
    }
}
