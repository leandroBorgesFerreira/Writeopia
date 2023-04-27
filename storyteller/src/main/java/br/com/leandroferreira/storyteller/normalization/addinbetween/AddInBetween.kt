package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import java.util.UUID

class AddInBetween(private val unitToAdd: StoryUnit) {

    fun insert(units: List<StoryUnit>): List<StoryUnit> =
        buildList {
            var acc = 0
            add(unitToAdd.copyWithNewPosition(acc++))

            addAll(
                units.flatMap { unit ->
                    listOf(
                        unit.copyWithNewPosition(acc++),
                        unitToAdd.copyWithNewPosition(acc++)
                    )
                }
            )
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
