package br.com.leandroferreira.storyteller.drawer

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import br.com.leandroferreira.storyteller.model.story.StoryUnit

/**
 * [StoryUnitDrawer] is the interface for drawing StoryUnits in the screen. It you would like
 * to support more types of StoryUnits are more implementation of this interface
 * to [StoryTellerTimeline]
 */
fun interface StoryUnitDrawer {
    @Composable
    fun LazyItemScope.Step(
        step: StoryUnit,
        drawInfo: DrawInfo
    )
}
