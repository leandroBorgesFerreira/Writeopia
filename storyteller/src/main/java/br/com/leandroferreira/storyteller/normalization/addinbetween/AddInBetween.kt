package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import java.util.UUID

class AddInBetween(private val unitToAdd: StoryUnit) {

    fun insert(units: List<StoryUnit>, filterRepeated: Boolean = true): List<StoryUnit> {
        val filteredUnits = units.fold(listOf<StoryUnit>()) { acc, storyUnit ->
            if (acc.isNotEmpty() &&
                acc.last().type == unitToAdd.type &&
                storyUnit.type == unitToAdd.type
            ) {
                acc
            } else {
                acc + storyUnit
            }
        }

        return buildList {
            var acc = 0
            var spaceAdded = false

            filteredUnits.forEach { storyUnit ->
                if (storyUnit.type == unitToAdd.type) {
                    if (!spaceAdded) {
                        spaceAdded = true
                        add(storyUnit.copyWithNewPosition(acc++))
                    }
                } else {
                    if (!spaceAdded) {
                        addAll(
                            listOf(
                                unitToAdd.copyWithNewPosition(acc++),
                                storyUnit.copyWithNewPosition(acc++),
                            )
                        )
                    } else {
                        add(storyUnit.copyWithNewPosition(acc++))
                    }

                    spaceAdded = false
                }
            }

            if (filteredUnits.last().type != unitToAdd.type) {
                add(unitToAdd.copyWithNewPosition(acc++))
            }
        }
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
