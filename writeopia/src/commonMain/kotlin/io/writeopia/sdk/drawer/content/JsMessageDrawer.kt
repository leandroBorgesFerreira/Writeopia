package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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

/**
 * Simple message drawer mostly intended to be used as a component for more complex drawers.
 * This class contains the logic of the basic message of the SDK. As many other drawers need some
 * text in it this Drawer can be used instead of duplicating this text logic.
 *
 * Important: This class is currently a duplication of [DesktopMessageDrawer] for JS due to compilation problems.
 * This class may be substituted for [DesktopMessageDrawer].
 */
class JsMessageDrawer(
    private val modifier: Modifier = Modifier,
    private val textStyle: TextStyle? = null,
    private val focusRequester: FocusRequester? = null,
    private val onKeyEvent: (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean = { _, _, _, _ -> false },
    private val onTextEdit: (Action.StoryStateChange) -> Unit = { },
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    override var onFocusChanged: (FocusState) -> Unit = {}
) : SimpleMessageDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        if (drawInfo.editable) {
            val text = step.text ?: ""
            val inputText = TextFieldValue(text, TextRange(text.length))


            if (drawInfo.focusId == step.id) {
                LaunchedEffect(step.localId) {
                    focusRequester?.requestFocus()
                }
            }

            BasicTextField(
                modifier = modifier
                    .onKeyEvent { keyEvent ->
                        onKeyEvent(keyEvent, inputText, step, drawInfo.position)
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
                    if (!changedText.contains("\n")) {
                        onTextEdit(Action.StoryStateChange(step.copy(text = changedText), drawInfo.position))
                        commandHandler.handleCommand(changedText, step, drawInfo.position)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = textStyle ?: TextStyle.Default
            )
        } else {
            Text(
                text = step.text ?: "",
                modifier = Modifier.padding(vertical = 5.dp),
            )
        }
    }
}

