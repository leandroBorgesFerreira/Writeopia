package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import java.util.UUID

class AddInBetween(private val unitToAdd: StoryUnit) {

    fun insert(units: List<StoryUnit>): List<StoryUnit> =
        buildList {
            var acc = 0

            if (units.first().type != unitToAdd.type) {
                add(unitToAdd.copyWithNewPosition(acc++))
            }

            units.forEach { storyUnit ->
                if (storyUnit.type != unitToAdd.type) {
                    addAll(
                        listOf(
                            storyUnit.copyWithNewPosition(acc++),
                            unitToAdd.copyWithNewPosition(acc++),
                        )
                    )
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
