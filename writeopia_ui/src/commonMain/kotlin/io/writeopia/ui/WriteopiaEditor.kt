package io.writeopia.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.writeopia.ui.draganddrop.target.DraggableScreen
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.ui.drawer.StoryStepDrawer

@Composable
fun WriteopiaEditor(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    editable: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    drawers: Map<Int, StoryStepDrawer>,
    storyState: DrawState
) {
    val content = storyState.stories.values.toList()

    DraggableScreen(modifier = modifier) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            state = listState,
            content = {
                itemsIndexed(
                    content,
                    key = { index, drawStory -> drawStory.key + index },
                    itemContent = { index, drawStory ->
                        drawers[drawStory.storyStep.type.number]?.Step(
                            step = drawStory.storyStep,
                            drawInfo = DrawInfo(
                                editable = editable,
                                focusId = storyState.focusId,
                                position = index,
                                extraData = mapOf("listSize" to storyState.stories.size),
                                selectMode = drawStory.isSelected
                            )
                        )
                    }
                )
            }
        )
    }
}
