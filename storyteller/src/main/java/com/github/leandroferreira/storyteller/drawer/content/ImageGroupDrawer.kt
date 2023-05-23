package com.github.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.github.leandroferreira.storyteller.drawer.DrawInfo
import com.github.leandroferreira.storyteller.drawer.StoryUnitDrawer
import com.github.leandroferreira.storyteller.model.story.GroupStep
import com.github.leandroferreira.storyteller.model.story.StoryStep
import com.github.leandroferreira.storyteller.model.story.StoryUnit

/**
 * Draws a scrollable list of images accordingly with the imageStepDrawer provided.
 */
class ImageGroupDrawer(
    private val imageStepDrawer: StoryUnitDrawer,
    private val modifier: Modifier = Modifier
) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(step: StoryUnit, drawInfo: DrawInfo) {
        val imageGroup = step as GroupStep
        val steps = imageGroup.steps.map { storyUnit -> storyUnit as StoryStep }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(drawInfo.focusId) {
            if (drawInfo.focusId == step.localId) {
                focusRequester.requestFocus()
            }
        }

        LazyRow(modifier = modifier.focusRequester(focusRequester)) {
            items(steps) { storyStep ->
                imageStepDrawer.run {
                    Step(storyStep, drawInfo = drawInfo.copy(focusId = null))
                }
            }
        }
    }
}
