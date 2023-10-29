package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
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
class MobileMessageDrawer(
    private val modifier: Modifier = Modifier,
    private val isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
    private val textStyle: @Composable (StoryStep) -> TextStyle = { defaultTextStyle(it) },
    private val focusRequester: FocusRequester? = null,
    private val onTextEdit: (Action.StoryStateChange)  -> Unit = {},
    private val emptyErase: ((Int) -> Unit)? = null,
    private val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    private val allowLineBreaks: Boolean = false,
    private val onLineBreak: (Action.LineBreak) -> Unit = {},
    override var onFocusChanged: (FocusState) -> Unit = {}
) : SimpleMessageDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        var inputText by remember {
            val text = step.text ?: ""
            mutableStateOf(TextFieldValue(text, TextRange(text.length)))
        }

        if (drawInfo.focusId == step.id) {
            LaunchedEffect(step.localId) {
                focusRequester?.requestFocus()
            }
        }

        if (drawInfo.editable) {
            BasicTextField(
                modifier = modifier.fillMaxWidth()
                    .padding(start = 6.dp)
                    .let { modifierLet ->
                        if (focusRequester != null) {
                            modifierLet.focusRequester(focusRequester)
                        } else {
                            modifierLet
                        }
                    }
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
                    .onFocusChanged(onFocusChanged)
                    .testTag("MobileMessageDrawer_${drawInfo.position}"),
                value = inputText,
                onValueChange = { value ->
                    val text = value.text
                    val newStep = step.copy(text = text)

                    inputText = if (text.contains("\n") && !allowLineBreaks) {
                        onLineBreak(Action.LineBreak(newStep, drawInfo.position))

                        val newText = text.split("\n", limit = 2)[0]
                        TextFieldValue(newText, TextRange(newText.length))
                    } else {
                        onTextEdit(Action.StoryStateChange(newStep, drawInfo.position))
                        commandHandler.handleCommand(text, newStep, drawInfo.position)

                        value
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                textStyle = textStyle(step),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            )
        } else {
            Text(
                text = step.text ?: "",
                modifier = Modifier.padding(vertical = 5.dp),
            )
        }
    }
}
