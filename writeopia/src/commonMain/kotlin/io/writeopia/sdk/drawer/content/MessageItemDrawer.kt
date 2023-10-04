package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Drawer for a complex message with swipe action, drag and drop logic and a start content to add functionality
 * like a checkbox or a different Composable.
 */
class MessageItemDrawer(
    private val modifier: Modifier = Modifier,
    private val customBackgroundColor: Color? = null,
    private val clickable: Boolean = true,
    private val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    private val focusRequester: FocusRequester? = null,
    private val startContent: @Composable ((StoryStep, DrawInfo) -> Unit)?,
    private val messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val dropInfo = DropInfo(step, drawInfo.position)
        var showDragIcon by remember { mutableStateOf(false) }

        SwipeBox(
            modifier = modifier
                .apply {
                    if (clickable) {
                        clickable {
                            focusRequester?.requestFocus()
                        }
                    }
                },
            defaultColor = customBackgroundColor ?: MaterialTheme.colorScheme.background,
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
                                focusRequester?.requestFocus()
                            }
                        }
                    },
                dataToDrop = dropInfo,
                showIcon = showDragIcon,
                position = drawInfo.position,
                emptySpaceClick = {
                    focusRequester?.requestFocus()
                }
            ) {
                startContent?.invoke(step, drawInfo)

                messageDrawer().apply {
                    onFocusChanged = { focusState ->
                        showDragIcon = focusState.hasFocus
                    }
                }.Step(step = step, drawInfo = drawInfo)
            }
        }
    }
}