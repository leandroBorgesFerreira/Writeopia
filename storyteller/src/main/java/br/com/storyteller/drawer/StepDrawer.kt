package br.com.storyteller.drawer

import androidx.compose.runtime.Composable
import br.com.storyteller.model.StoryStep

fun interface StepDrawer {
    @Composable
    fun Step(step: StoryStep)
}
