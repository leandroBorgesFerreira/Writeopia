package com.github.leandroborgesferreira.storyteller.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.github.leandroborgesferreira.storyteller.draganddrop.target.DragTargetWithDragItem
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.draganddrop.DropInfo
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.text.edition.TextCommandHandler
import com.github.leandroborgesferreira.storyteller.uicomponents.SwipeBox

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
    private val simpleMessageDrawer: @Composable (FocusRequester) -> SimpleMessageDrawer = {
        SimpleMessageDrawer(
            textModifier = textModifier,
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
            simpleMessageDrawer(focusRequester).Step(step = step, drawInfo = drawInfo)
        }
    }
}


@Composable
internal fun defaultTextStyle() =
    TextStyle(
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp
    )

@Preview
@Composable
private fun MessageDrawerPreview() {
    SwipeMessageDrawer().Step(
        step = StoryStep(text = "Some text", type = StoryTypes.MESSAGE.type),
        drawInfo = DrawInfo()
    )
}