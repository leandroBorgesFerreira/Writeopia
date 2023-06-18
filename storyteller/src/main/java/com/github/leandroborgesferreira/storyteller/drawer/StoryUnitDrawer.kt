package com.github.leandroborgesferreira.storyteller.drawer

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

/**
 * [StoryUnitDrawer] is the interface for drawing StoryUnits in the screen. It you would like
 * to support more types of StoryUnits are more implementation of this interface
 * to [StoryTellerTimeline]
 */
fun interface StoryUnitDrawer {
    @Composable
    fun Step(
        step: StoryStep,
        drawInfo: DrawInfo
    )
}
