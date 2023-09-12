package com.storiesteller.sdk

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.storiesteller.sdk.draganddrop.target.DraggableScreen
import com.storiesteller.sdk.drawer.DrawInfo
import com.storiesteller.sdk.drawer.StoryStepDrawer
import com.storiesteller.sdk.model.story.DrawState

@Composable
fun StoriesTellerEditor(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    editable: Boolean,
    storyState: DrawState,
    drawers: Map<Int, StoryStepDrawer>,
    listState: LazyListState = rememberLazyListState()
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
                        drawers[drawStory.storyStep.type.number]?.run {
                            Step(
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
                    }
                )
            })
    }
}
