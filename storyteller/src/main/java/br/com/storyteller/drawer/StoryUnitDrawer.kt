package br.com.storyteller.drawer

import androidx.compose.runtime.Composable
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

fun interface StoryUnitDrawer {
    @Composable
    fun Step(step: StoryUnit)
}
