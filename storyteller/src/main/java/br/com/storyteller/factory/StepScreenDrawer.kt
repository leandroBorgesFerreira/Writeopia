package br.com.storyteller.factory

import androidx.compose.runtime.Composable
import br.com.storyteller.model.StoryStep

interface StepScreenDrawer {
    @Composable
    fun Screen(step: StoryStep): Unit
}
