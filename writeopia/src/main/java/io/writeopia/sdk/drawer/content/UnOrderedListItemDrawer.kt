package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.DrawInfo
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Drawer for a unordered list. This type of item it just a normal message with some decoration
 * at the start of Composable to show that this is part of a list.
 */
class UnOrderedListItemDrawer(
    private val modifier: Modifier = Modifier,
    private val messageModifier: Modifier = Modifier,
    private val startText: String = "-",
    private val customBackgroundColor: Color? = null,
    private val clickable: Boolean = true,
    private val textStyle: @Composable () -> TextStyle = {
        LocalTextStyle.current
    },
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    private val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    private val emptyErase: (Int) -> Unit = {},
    private val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val focusRequester = remember { FocusRequester() }
        val dropInfo = DropInfo(step, drawInfo.position)
        var showDragIcon by remember { mutableStateOf(true) }

        SwipeBox(
            modifier = modifier
                .apply {
                    if (clickable) {
                        clickable {
                            focusRequester.requestFocus()
                        }
                    }
                },
            defaultColor = customBackgroundColor ?: MaterialTheme.colorScheme.background,
            activeColor = MaterialTheme.colorScheme.primary,
            state = drawInfo.selectMode,
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
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = startText,
                    style = textStyle()
                )

                SimpleMessageDrawer(
                    modifier = messageModifier.weight(1F),
                    textModifier = Modifier.fillMaxWidth(),
                    focusRequester = focusRequester,
                    commandHandler = commandHandler,
                    emptyErase = emptyErase,
                    onTextEdit = onTextEdit,
                    onFocusChanged = { focusState ->
                        showDragIcon = focusState.hasFocus
                    }
                ).Step(step = step, drawInfo = drawInfo)
            }
        }
    }
}

@Preview
@Composable
private fun UnOrderedListItemPreview() {
    val modifier = Modifier
        .background(Color.Cyan)
        .padding(vertical = 4.dp, horizontal = 6.dp)
        .fillMaxWidth()

    val modifierMessage = Modifier
        .background(Color.Cyan)
        .fillMaxWidth()

    UnOrderedListItemDrawer(modifier, messageModifier = modifierMessage).Step(
        StoryStep(type = StoryTypes.UNORDERED_LIST_ITEM.type, text = "Item1"),
        DrawInfo()
    )
}