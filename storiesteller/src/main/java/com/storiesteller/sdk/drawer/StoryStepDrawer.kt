package com.storiesteller.sdk.drawer

import androidx.compose.runtime.Composable
import com.storiesteller.sdk.models.story.StoryStep

/**
 * [StoryStepDrawer] is the interface for drawing StoryUnits in the screen. It you would like
 * to support more types of StoryUnits are more implementation of this interface
 * to [StoryTellerTimeline]
 */
fun interface StoryStepDrawer {
    @Composable
    fun Step(step: StoryStep, drawInfo: DrawInfo)
}
