package br.com.storyteller

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

@Composable
fun StoryTellerTimeline(
    modifier: Modifier = Modifier,
    drawers: Map<String, StoryUnitDrawer>,
    story: List<StoryUnit>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onStepsUpdate: (List<StoryStep>) -> Unit = {}
) {
    LazyColumn(modifier = modifier, contentPadding = contentPadding, content = {
        items(story) { storyStep ->
            drawers[storyStep.type]?.Step(storyStep)
        }
    })
}
