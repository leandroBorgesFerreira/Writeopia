package io.writeopia.ui.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.extensions.toTextRange
import io.writeopia.ui.model.DrawInfo
import io.writeopia.ui.model.EmptyErase
import io.writeopia.ui.model.TextInput
import io.writeopia.ui.utils.defaultTextStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private val onTextEdit: (TextInput, Int, Boolean, Boolean) -> Unit = { _, _, _, _ -> },
    private val allowLineBreaks: Boolean = false,
    private val lineBreakByContent: Boolean = true,
    private val emptyErase: EmptyErase = EmptyErase.CHANGE_TYPE,
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
        val text = step.text ?: ""
        val inputText = TextFieldValue(text, drawInfo.selection.toTextRange())

        var textLayoutResult by remember {
            mutableStateOf<TextLayoutResult?>(null)
        }
        val cursorLine by remember {
            derivedStateOf {
                textLayoutResult?.getLineForOffset(inputText.selection.start)
            }
        }
        val realPosition by remember {
            derivedStateOf {
                val lineStart = textLayoutResult?.multiParagraph?.getLineStart(cursorLine ?: 0)
                inputText.selection.start - (lineStart ?: 0)
            }
        }
        val isInLastLine by remember {
            derivedStateOf {
                textLayoutResult?.multiParagraph?.lineCount == cursorLine
            }
        }

        val selectionState by selectionState.collectAsState()

        if (drawInfo.hasFocus()) {
            LaunchedEffect(step.localId) {
                focusRequester?.requestFocus()
            }
        }

        val coroutineScope = rememberCoroutineScope()

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
                .onPreviewKeyEvent { keyEvent ->
                    println("keyEvent. ${keyEvent.key.keyCode}")
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
            onTextLayout = {
                textLayoutResult = it
            },
            onValueChange = { value ->
                val text = value.text
                val start = value.selection.start
                val end = value.selection.end

                val edit = {
                    println("realPosition: $realPosition")
                    onTextEdit(
                        TextInput(
                            value.text,
                            value.selection.start,
                            value.selection.end
                        ),
                        drawInfo.position,
                        lineBreakByContent,
                        allowLineBreaks
                    )
                }

                if (text.isEmpty() && start == 0 && end == 0) {
                    coroutineScope.launch {
                        // Delay to avoid jumping to previous line when erasing text
                        delay(70)
                        edit()
                    }
                } else {
                    edit()
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
