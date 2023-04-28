package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import java.util.Stack
import java.util.UUID

class AddInBetween(private val unitToAdd: StoryUnit) {

    fun insert(units: List<StoryUnit>): List<StoryUnit> {
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
