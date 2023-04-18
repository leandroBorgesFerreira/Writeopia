package br.com.leandroferreira.storyteller

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryUnit

@Composable
fun StoryTellerTimeline(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    editable: Boolean,
    story: List<StoryUnit>,
    drawers: Map<String, StoryUnitDrawer>
) {
    LazyColumn(modifier = modifier, contentPadding = contentPadding, content = {
        items(
            story,
            key = { storyUnit -> storyUnit.key }
        ) { storyUnit ->
            drawers[storyUnit.type]?.Step(storyUnit, editable, mapOf("listSize" to story.size))
        }
    })
}
