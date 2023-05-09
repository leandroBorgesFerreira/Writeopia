package br.com.leandroferreira.storyteller.drawer.content

import android.view.KeyEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.DrawInfo
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class MessageStepDrawer(
    private val containerModifier: Modifier = Modifier,
    private val innerContainerModifier: Modifier = Modifier,
    private val onTextEdit: (String, Int) -> Unit,
    private val onLineBreak: (LineBreakInfo) -> Unit,
    private val onDeleteRequest: (DeleteInfo) -> Unit
) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(step: StoryUnit, drawInfo: DrawInfo) {
        val messageStep = step as StoryStep

        Box(modifier = containerModifier) {
            if (drawInfo.editable) {
                var inputText by remember {
                    val text = messageStep.text ?: ""
                    mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                }
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(drawInfo.focusId) {
                    if (drawInfo.focusId == step.localId) {
                        focusRequester.requestFocus()
                    }
                }

                BasicTextField(
                    modifier = innerContainerModifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL &&
                                inputText.selection.start == 0
                            ) {
                                onDeleteRequest(DeleteInfo(step, drawInfo.position))
                                true
                            } else {
                                false
                            }
                        },
                    value = inputText,
                    onValueChange = { value ->
                        if (value.text.contains("\n")) {
                            onLineBreak(
                                LineBreakInfo(
                                    step.copy(text = value.text),
                                    position = drawInfo.position
                                )
                            )
                        } else {
                            inputText = value
                            onTextEdit(value.text, drawInfo.position)
                        }
                    },
                )
            } else {
                Text(
                    text = messageStep.text ?: "",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
        }
    }
}
