package br.com.storyteller.drawer.content

import androidx.compose.runtime.Composable
import br.com.storyteller.drawer.StepDrawer
import br.com.storyteller.model.StoryStep

fun interface CompositeDrawer {

    @Composable
    fun Step(step: StoryStep, drawer: StepDrawer)
}
