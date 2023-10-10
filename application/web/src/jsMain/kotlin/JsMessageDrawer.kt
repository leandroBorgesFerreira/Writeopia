import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.TextRange
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
 */
class JsMessageDrawer(
    private val modifier: Modifier = Modifier,
    private val focusRequester: FocusRequester? = null,
    private val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    private val emptyErase: ((Int) -> Unit)? = null,
    private val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    override var onFocusChanged: (FocusState) -> Unit = {}
) : SimpleMessageDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Box(modifier = modifier) {
            if (drawInfo.editable) {
                var inputText by remember {
                    val text = step.text ?: ""
                    mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                }

                LaunchedEffect(drawInfo.focusId) {
                    if (drawInfo.focusId == step.id) {
                        focusRequester?.requestFocus()
                    }
                }

                BasicTextField(
                    modifier = Modifier
                        .padding(start = 16.dp)
//                        .onKeyEvent { keyEvent ->
//                            if (
//                                keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE &&
//                                inputText.selection.start == 0
//                            ) {
//                                emptyErase?.invoke(drawInfo.position) ?: onDeleteRequest(
//                                    Action.DeleteStory(
//                                        step,
//                                        drawInfo.position
//                                    )
//                                )
//
//                                true
//                            } else {
//                                false
//                            }
//                        }
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
                        val text = value.text

                        inputText = if (text.contains("\n")) {
                            val newText = text.split("\n", limit = 2)[0]
                            TextFieldValue(newText, TextRange(newText.length))
                        } else {
                            value
                        }

                        onTextEdit(value.text, drawInfo.position)
                        commandHandler.handleCommand(text, step, drawInfo.position)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            } else {
                Text(
                    text = step.text ?: "",
                    modifier = Modifier.padding(vertical = 5.dp),
                )
            }
        }
    }
}

