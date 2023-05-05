package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.utils.extensions.associateWithPosition
import java.util.Stack
import java.util.UUID

/**
 * This normalizer will guarantee that a new StoryUnit is added between all items of
 * a List<StoryUnit> this normalizer can be very useful to insert spaces between a list
 * of StoryUnits.
 */
class AddInBetween(private val unitToAdd: StoryUnit) {

    fun insert(unit: Map<Int, StoryUnit>): Map<Int, StoryUnit> =
        insert(unit.values).associateWithPosition()

    fun insert(units: Iterable<StoryUnit>): List<StoryUnit> {
        val stack: Stack<StoryUnit> = Stack()
        var acc = 0

        val typeToAdd = unitToAdd.type

        units.forEach { storyUnit ->
            when {
                stack.isEmpty() && storyUnit.type != typeToAdd -> {
                    stack.add(unitToAdd.copyWithNewPosition(acc++))
                    stack.add(storyUnit.copyWithNewPosition(acc++))
                }

                stack.isEmpty() && storyUnit.type == typeToAdd -> {
                    stack.add(storyUnit.copyWithNewPosition(acc++))
                }

                storyUnit.type != typeToAdd && stack.peek().type == typeToAdd -> {
                    stack.add(storyUnit.copyWithNewPosition(acc++))
                }

                storyUnit.type == typeToAdd && stack.peek()?.type != typeToAdd -> {
                    stack.add(storyUnit.copyWithNewPosition(acc++))
                }

                storyUnit.type != typeToAdd && stack.peek()?.type != typeToAdd -> {
                    stack.add(unitToAdd.copyWithNewPosition(acc++))
                    stack.add(storyUnit.copyWithNewPosition(acc++))
                }

                storyUnit.type == typeToAdd && stack.peek()?.type == typeToAdd -> {}
            }
        }

        if (stack.peek().type != typeToAdd) {
            stack.add(unitToAdd.copyWithNewPosition(acc++))
        }

        return stack.toList()
    }


    companion object {
        fun spaces(): AddInBetween =
            AddInBetween(
                unitToAdd = StoryStep(
                    id = UUID.randomUUID().toString(),
                    type = "space",
                    localPosition = 0
                )
            )
    }
}
