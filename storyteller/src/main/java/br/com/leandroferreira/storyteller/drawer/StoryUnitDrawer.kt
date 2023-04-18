package br.com.leandroferreira.storyteller.drawer

import androidx.compose.runtime.Composable
import br.com.leandroferreira.storyteller.model.StoryUnit

fun interface StoryUnitDrawer {
    @Composable
    fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>)
}
