package br.com.storyteller.drawer.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

class ImageGroupDrawer(
    private val imageStepDrawer: ImageStepDrawer,
    private val modifier: Modifier = Modifier
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit) {
        val imageGroup = step as GroupStep
        val steps = imageGroup.steps.map { storyUnit -> storyUnit as StoryStep }

        LazyRow(modifier = modifier) {
            items(steps) { storyStep ->
                imageStepDrawer.Step(storyStep)
            }
        }
    }

}
