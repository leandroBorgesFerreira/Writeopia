package io.writeopia.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.story.Selection
import io.writeopia.ui.model.DrawInfo
import io.writeopia.ui.model.DrawState
import io.writeopia.ui.draganddrop.target.DraggableScreen
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.model.DrawStory

@Composable
fun WriteopiaEditor(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    editable: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    drawers: Map<Int, StoryStepDrawer>,
    storyState: DrawState,
    keyFn: (Int, DrawStory) -> Int = { index, drawStory -> drawStory.desktopKey + index }
) {
    val content = storyState.stories
        .filterNot { draw ->
            draw.storyStep.tags.any { it.tag.isHidden() }
        }

    DraggableScreen(modifier = modifier) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            state = listState,
            content = {
                itemsIndexed(
                    content,
                    key = keyFn,
                    itemContent = { _, drawStory ->
                        drawers[drawStory.storyStep.type.number]?.Step(
                            step = drawStory.storyStep,
                            drawInfo = DrawInfo(
                                editable = editable,
                                focus = storyState.focus,
                                position = drawStory.position,
                                extraData = mapOf("listSize" to storyState.stories.size),
                                selectMode = drawStory.isSelected,
                                selection = drawStory.cursor ?: Selection.start()
                            )
                        )
                    }
                )
            }
        )
    }
}
