package com.github.leandroborgesferreira.storyteller.drawer.content

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
import com.github.leandroborgesferreira.storyteller.draganddrop.target.DragTargetWithDragItem
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.decoration.ListItemDecoratorDrawer
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.draganddrop.DropInfo
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.text.edition.TextCommandHandler
import com.github.leandroborgesferreira.storyteller.uicomponents.SwipeBox

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
    private val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
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
                position = drawInfo.position
            ) {
                Text(modifier = Modifier.padding(horizontal = 8.dp), text = startText, style = textStyle())

                SimpleMessageDrawer(
                    modifier = messageModifier.weight(1F),
                    textModifier = Modifier.fillMaxWidth(),
                    focusRequester = focusRequester,
                    commandHandler = commandHandler,
                    onDeleteRequest = onDeleteRequest,
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