package br.com.storyteller.drawer.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

class MessageGroupDrawer(
    private val messageStepDrawer: MessageStepDrawer,
    private val modifier: Modifier = Modifier
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit) {
        val imageGroup = step as GroupStep
        val steps = imageGroup.steps.map { storyUnit -> storyUnit as StoryStep }

        Column(modifier = modifier) {
            steps.forEach { storyStep ->
                messageStepDrawer.Step(storyStep)
                Spacer(modifier = modifier.height(6.dp))
            }
        }
    }
}
