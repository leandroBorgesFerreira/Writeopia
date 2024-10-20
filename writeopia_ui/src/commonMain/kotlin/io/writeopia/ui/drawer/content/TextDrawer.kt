package io.writeopia.ui.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.edition.TextCommandHandler
import io.writeopia.ui.model.EmptyErase
import io.writeopia.ui.utils.defaultTextStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Simple message drawer intended to be used as a component for more complex drawers.
 * This class contains the logic of the basic message of the SDK. As many other drawers need some
 * text in it this Drawer can be used instead of duplicating this text logic.
 */
class TextDrawer(
    private val modifier: Modifier = Modifier,
    private val onKeyEvent: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase) -> Boolean =
        { _, _, _, _, _ -> false },
    private val textStyle: @Composable (StoryStep) -> TextStyle = { defaultTextStyle(it) },
    private val onTextEdit: (Action.StoryStateChange) -> Unit = { },
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    private val allowLineBreaks: Boolean = false,
    private val lineBreakByContent: Boolean = true,
    private val emptyErase: EmptyErase = EmptyErase.CHANGE_TYPE,
    private val onLineBreak: (Action.LineBreak) -> Unit = {},
    override var onFocusChanged: (Int, FocusState) -> Unit = { _, _ -> },
    private val selectionState: StateFlow<Boolean>,
    private val onSelectionLister: (Int) -> Unit
) : SimpleTextDrawer {

    @Composable
    override fun Text(
        step: StoryStep,
        drawInfo: DrawInfo,
        interactionSource: MutableInteractionSource,
        focusRequester: FocusRequester?,
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit
    ) {
        var inputText by remember {
            val text = step.text ?: ""
            mutableStateOf(TextFieldValue(text, TextRange(text.length)))
        }

        val selectionState by selectionState.collectAsState()

        if (drawInfo.focusId == step.id) {
            LaunchedEffect(step.localId) {
                focusRequester?.requestFocus()
            }
        }

        BasicTextField(
            modifier = modifier
                .padding(start = 16.dp)
                .let { modifierLet ->
                    if (focusRequester != null) {
                        modifierLet.focusRequester(focusRequester)
                    } else {
                        modifierLet
                    }
                }
                .onKeyEvent { keyEvent ->
                    onKeyEvent(keyEvent, inputText, step, drawInfo.position, emptyErase)
                }
                .onFocusChanged { focusState ->
                    onFocusChanged(drawInfo.position, focusState)
                }
                .testTag("MessageDrawer_${drawInfo.position}")
                .let { modifierLet ->
                    if (selectionState) {
                        modifierLet.clickable { onSelectionLister(drawInfo.position) }
                    } else {
                        modifierLet
                    }
                },
            value = inputText,
            enabled = !selectionState,
            onValueChange = { value ->
                val text = value.text

                inputText = if (lineBreakByContent && !allowLineBreaks && text.contains("\n")) {
                    val newStep = step.copy(text = text)
                    onLineBreak(Action.LineBreak(newStep, drawInfo.position))

                    val newText = text.split("\n", limit = 2)[0]
                    TextFieldValue(newText, TextRange(newText.length))
                } else {
                    val newText = if (allowLineBreaks) {
                        text
                    } else {
                        text.replace("\n", "")
                    }
                    val newStep = step.copy(text = newText)
                    onTextEdit(Action.StoryStateChange(newStep, drawInfo.position))
                    commandHandler.handleCommand(text, newStep, drawInfo.position)

                    value.copy(text = newText)
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            textStyle = textStyle(step),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
            decorationBox = decorationBox,
        )
    }
}

@Preview
@Composable
fun DesktopMessageDrawerPreview() {
    TextDrawer(selectionState = MutableStateFlow(false), onSelectionLister = {}).Text(
        step = StoryStep(text = "Some text", type = StoryTypes.TEXT.type),
        drawInfo = DrawInfo(),
        interactionSource = remember { MutableInteractionSource() },
        focusRequester = FocusRequester(),
        decorationBox = @Composable { innerTextField -> innerTextField() }
    )
}
