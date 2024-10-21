package io.writeopia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.ui.model.DrawState
import io.writeopia.ui.draganddrop.target.DraggableScreen
import io.writeopia.ui.drawer.StoryStepDrawer

@Composable
fun WriteopiaEditorBox(
    modifier: Modifier = Modifier,
    editable: Boolean = true,
    drawers: Map<Int, StoryStepDrawer>,
    storyState: DrawState = DrawState(emptyMap())
) {
    DraggableScreen(modifier = modifier) {
        Column(
            modifier = modifier,
            content = {
                storyState.stories.values.toList().forEachIndexed { index, drawStory ->
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
            }
        )
    }
}
