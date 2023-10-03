package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.uicomponents.SwipeBox
import io.writeopia.sdk.utils.ui.defaultTextStyle

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class SwipeMessageDrawer(
    private val modifier: Modifier = Modifier,
    private val textModifier: Modifier = Modifier,
    private val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    private val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    private val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    private val customBackgroundColor: Color? = null,
    private val clickable: Boolean = true,
    private val textStyle: @Composable () -> TextStyle = { defaultTextStyle() },
    private val simpleMessageDrawer: @Composable RowScope.(FocusRequester) -> SimpleMessageDrawer = {
        SimpleMessageDrawer(
            modifier = Modifier.weight(1F),
            textModifier = textModifier.fillMaxWidth(),
            onTextEdit = onTextEdit,
            onDeleteRequest = onDeleteRequest,
            commandHandler = commandHandler,
            textStyle = textStyle,
            focusRequester = it
        )
    }
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val focusRequester = remember { FocusRequester() }
        val dropInfo = DropInfo(step, drawInfo.position)
        var showDragIcon by remember { mutableStateOf(false) }

        SwipeBox(
            modifier = modifier.fillMaxWidth(),
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
                                focusRequester.requestFocus()
                            }
                        }
                    },
                dataToDrop = dropInfo,
                showIcon = showDragIcon,
                position = drawInfo.position,
                emptySpaceClick = focusRequester::requestFocus
            ) {
                simpleMessageDrawer(focusRequester).apply {
                    onFocusChanged = { focusState ->
                        showDragIcon = focusState.hasFocus
                    }
                }.Step(step = step, drawInfo = drawInfo)
            }
        }
    }
}