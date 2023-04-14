package br.com.storyteller

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.storyteller.factory.MessageStepScreenDrawer
import br.com.storyteller.factory.StepScreenDrawer
import br.com.storyteller.model.StepType
import br.com.storyteller.model.StoryStep

@Composable
fun StoryTellerTimeline(
    modifier: Modifier = Modifier,
    drawers: Map<String, StepScreenDrawer> = defaultDrawers(),
    steps: List<StoryStep>
) {

    LazyColumn(modifier = modifier, content = {
        items(steps) { storyStep ->
            drawers[storyStep.type]?.Screen(storyStep)
        }
    })
}

fun defaultDrawers(): Map<String, StepScreenDrawer> {
    return buildMap {
        put(StepType.MESSAGE.type, MessageStepScreenDrawer())
    }
}
