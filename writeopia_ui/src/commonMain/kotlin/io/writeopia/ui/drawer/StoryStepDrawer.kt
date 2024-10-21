package io.writeopia.ui.drawer

import androidx.compose.runtime.Composable
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

/**
 * [StoryStepDrawer] is the interface for drawing [StoryStep] in the screen. It you would like
 * to support more types of [StoryStep] are more implementation of this interface
 * to [StoryStepDrawer]
 */
fun interface StoryStepDrawer {

    /**
     * Draws the StoryStep including its [DrawInfo]
     *
     * @param step [StoryStep]
     * @param drawInfo [DrawInfo]
     */
    @Composable
    fun Step(step: StoryStep, drawInfo: DrawInfo)
}
