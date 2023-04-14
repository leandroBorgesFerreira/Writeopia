package br.com.storyteller

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.storyteller.drawer.AddButtonDrawer
import br.com.storyteller.drawer.MessageStepDrawer
import br.com.storyteller.drawer.StepDrawer
import br.com.storyteller.model.StepType
import br.com.storyteller.model.StoryStep

@Composable
fun StoryTellerTimeline(
    modifier: Modifier = Modifier,
    drawers: Map<String, StepDrawer> = defaultDrawers(),
    steps: List<StoryStep>
) {

    LazyColumn(modifier = modifier, content = {
        items(steps) { storyStep ->
            drawers[storyStep.type]?.Step(storyStep)
        }
    })
}

fun defaultDrawers(): Map<String, StepDrawer> {
    return buildMap {
        put(StepType.MESSAGE.type, MessageStepDrawer())
        put(StepType.ADD_BUTTON.type, AddButtonDrawer())
    }
}
