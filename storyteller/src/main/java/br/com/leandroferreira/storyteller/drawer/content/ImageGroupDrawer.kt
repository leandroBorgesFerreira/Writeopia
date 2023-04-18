package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit

class ImageGroupDrawer(
    private val imageStepDrawer: StoryUnitDrawer,
    private val modifier: Modifier = Modifier
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        val imageGroup = step as GroupStep
        val steps = imageGroup.steps.map { storyUnit -> storyUnit as StoryStep }

        LazyRow(modifier = modifier) {
            items(steps) { storyStep ->
                imageStepDrawer.Step(storyStep, editable = editable, extraData)
            }
        }
    }
}
