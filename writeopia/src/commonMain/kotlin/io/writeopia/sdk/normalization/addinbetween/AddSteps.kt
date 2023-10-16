package io.writeopia.sdk.normalization.addinbetween

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.utils.extensions.associateWithPosition

/**
 * This normalizer will guarantee that a new StoryUnit is added between all items of
 * a List<StoryUnit> this normalizer can be very useful to insert spaces between a list
 * of StoryUnits.
 */
class AddSteps(
    private val addInBetween: () -> StoryStep,
    private val addAtLast: () -> StoryStep,
    private val skipFirst: Boolean = false
) {

    fun insert(unit: Map<Int, StoryStep>): Map<Int, StoryStep> =
        insert(unit.values).associateWithPosition()

    fun insert(units: Iterable<StoryStep>): List<StoryStep> {
        val stack: MutableList<StoryStep> = mutableListOf()
        val typeToAdd = addInBetween().type
        val typeAtLast = addAtLast().type

        units.forEach { storyUnit ->
            when {
                storyUnit.type == typeAtLast -> {
                    if (stack.last().type != typeToAdd) {
                        stack.add(addInBetween())
                    }
                    stack.add(addAtLast())
                }

                stack.isEmpty() && storyUnit.type != typeToAdd -> {
                    if (!skipFirst) {
                        stack.add(addInBetween())
                    }
                    stack.add(storyUnit.copy(localId = GenerateId.generate()))
                }

                stack.isEmpty() && storyUnit.type == typeToAdd -> {
                    stack.add(storyUnit.copy(localId = GenerateId.generate()))
                }

                storyUnit.type != typeToAdd && stack.lastOrNull()?.type == typeToAdd -> {
                    stack.add(storyUnit.copy(localId = GenerateId.generate()))
                }

                storyUnit.type == typeToAdd && stack.lastOrNull()?.type != typeToAdd -> {
                    stack.add(storyUnit.copy(localId = GenerateId.generate()))
                }

                storyUnit.type != typeToAdd && stack.lastOrNull()?.type != typeToAdd -> {
                    stack.add(addInBetween())
                    stack.add(storyUnit.copy(localId = GenerateId.generate()))
                }

                storyUnit.type == typeToAdd && stack.lastOrNull()?.type == typeToAdd -> {}
            }
        }

        val lastElement = stack.lastOrNull()

        if (lastElement?.type != typeAtLast) {
            if (lastElement?.type != typeToAdd) {
                stack.add(addInBetween())
            }

            stack.add(addAtLast())
        }

        return stack.toList()
    }


    companion object {
        fun spaces(skipFirst: Boolean): AddSteps =
            AddSteps(
                addInBetween = {
                    StoryStep(type = StoryTypes.SPACE.type)
                },
                addAtLast = {
                    StoryStep(type = StoryTypes.LAST_SPACE.type)
                },
                skipFirst = skipFirst
            )
    }
}
