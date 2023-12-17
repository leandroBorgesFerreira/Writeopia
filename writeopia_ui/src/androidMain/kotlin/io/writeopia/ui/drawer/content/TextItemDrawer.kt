package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.SimpleTextDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Drawer for a complex message with swipe action, drag and drop logic and a start content to add functionality
 * like a checkbox or a different Composable.
 */
actual class TextItemDrawer actual constructor(
    private val modifier: Modifier,
    private val customBackgroundColor: Color,
    private val clickable: Boolean,
    private val onSelected: (Boolean, Int) -> Unit,
    private val dragIconWidth: Dp,
    private val startContent: @Composable ((StoryStep, DrawInfo) -> Unit)?,
    private val messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val dropInfo = DropInfo(step, drawInfo.position)
        var showDragIcon by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }

        SwipeBox(
            modifier = modifier
                .apply {
                    if (clickable) {
                        clickable {
                            focusRequester.requestFocus()
                        }
                    }
                },
            defaultColor = customBackgroundColor,
            activeColor = MaterialTheme.colorScheme.primary,
            isOnEditState = drawInfo.selectMode,
            swipeListener = { isSelected ->
                onSelected(isSelected, drawInfo.position)
            }
        ) {
            DragTargetWithDragItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .apply {
                        if (clickable) {
                            clickable {
                                focusRequester.requestFocus()
                            }
                        }
                    },
                dataToDrop = dropInfo,
                showIcon = showDragIcon,
                position = drawInfo.position,
                dragIconWidth = dragIconWidth,
                emptySpaceClick = {
                    focusRequester.requestFocus()
                }
            ) {
                startContent?.invoke(step, drawInfo)

                val interactionSource = remember { MutableInteractionSource() }
                messageDrawer().apply {
                    onFocusChanged = { focusState ->
                        showDragIcon = focusState.hasFocus
                    }
                }.Text(
                    step = step,
                    drawInfo = drawInfo,
                    interactionSource = interactionSource,
                    focusRequester = focusRequester,
                    decorationBox = @Composable { innerTextField -> innerTextField() }
                )
            }
        }
    }
}