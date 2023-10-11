package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.utils.ui.defaultTextStyle

/**
 * Simple message drawer mostly intended to be used as a component for more complex drawers.
 * This class contains the logic of the basic message of the SDK. As many other drawers need some
 * text in it this Drawer can be used instead of duplicating this text logic.
 */
class DesktopMessageDrawer(
    private val modifier: Modifier = Modifier,
    private val isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
    private val textStyle: @Composable (StoryStep) -> TextStyle = { defaultTextStyle(it) },
    private val focusRequester: FocusRequester? = null,
    private val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    private val emptyErase: ((Int) -> Unit)? = null,
    private val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    override var onFocusChanged: (FocusState) -> Unit = {}
) : SimpleMessageDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        if (drawInfo.editable) {
            val text = step.text ?: ""
            val inputText = TextFieldValue(text, TextRange(text.length))

            LaunchedEffect(drawInfo.focusId) {
                if (drawInfo.focusId == step.id) {
                    focusRequester?.requestFocus()
                }
            }

            BasicTextField(
                modifier = modifier
                    .padding(start = 16.dp)
                    .onKeyEvent { keyEvent ->
                        if (isEmptyErase(keyEvent, inputText)) {
                            emptyErase?.invoke(drawInfo.position) ?: onDeleteRequest(
                                Action.DeleteStory(
                                    step,
                                    drawInfo.position
                                )
                            )

                            true
                        } else {
                            false
                        }
                    }
                    .let { modifierLet ->
                        if (focusRequester != null) {
                            modifierLet.focusRequester(focusRequester)
                        } else {
                            modifierLet
                        }
                    }
                    .onFocusChanged(onFocusChanged),
                value = inputText,
                onValueChange = { value ->
                    val changedText = value.text
                    onTextEdit(changedText, drawInfo.position)
                    commandHandler.handleCommand(changedText, step, drawInfo.position)
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                textStyle = textStyle(step),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
        } else {
            Text(
                text = step.text ?: "",
                modifier = modifier.padding(vertical = 5.dp),
            )
        }
    }
}
