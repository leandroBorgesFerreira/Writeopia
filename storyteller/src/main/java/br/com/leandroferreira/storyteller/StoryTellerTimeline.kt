package br.com.leandroferreira.storyteller

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.target.DraggableScreen
import br.com.leandroferreira.storyteller.drawer.DrawInfo
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.story.StoryState

@Composable
fun StoryTellerTimeline(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    editable: Boolean,
    storyState: StoryState,
    drawers: Map<String, StoryUnitDrawer>,
    listState: LazyListState = rememberLazyListState()
) {
    DraggableScreen(modifier = modifier) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            state = listState,
            content = {
                itemsIndexed(
                    storyState.stories.values.toList(),
                    key = { _, storyUnit -> storyUnit.key },
                    itemContent = {index, storyUnit ->
                        drawers[storyUnit.type]?.run {
                            Step(
                                step = storyUnit,
                                drawInfo = DrawInfo(
                                    editable = editable,
                                    focusId = storyState.focusId,
                                    position = index,
                                    extraData = mapOf("listSize" to storyState.stories.size)
                                )
                            )
                        }
                    }
                )
            })
    }
}
