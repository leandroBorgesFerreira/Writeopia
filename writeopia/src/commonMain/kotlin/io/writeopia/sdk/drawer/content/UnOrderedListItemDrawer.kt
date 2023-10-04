package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Drawer for a unordered list. This type of item it just a normal message with some decoration
 * at the start of Composable to show that this is part of a list.
 */
class UnOrderedListItemDrawer(
    private val modifier: Modifier = Modifier,
    private val startText: String = "-",
    private val customBackgroundColor: Color? = null,
    private val clickable: Boolean = true,
    private val textStyle: @Composable () -> TextStyle = { LocalTextStyle.current },
    private val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    private val focusRequester: FocusRequester? = null,
    private val messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val dropInfo = DropInfo(step, drawInfo.position)
        var showDragIcon by remember { mutableStateOf(true) }

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
                emptySpaceClick =  {
                    focusRequester?.requestFocus()
                }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = startText,
                    style = textStyle()
                )

                messageDrawer().apply {
                    onFocusChanged = { focusState ->
                        showDragIcon = focusState.hasFocus
                    }
                }.Step(step = step, drawInfo = drawInfo)
            }
        }
    }
}