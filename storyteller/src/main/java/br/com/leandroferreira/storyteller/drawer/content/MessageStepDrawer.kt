package br.com.leandroferreira.storyteller.drawer.content

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
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class MessageStepDrawer(
    private val containerModifier: Modifier = Modifier,
    private val innerContainerModifier: Modifier = Modifier,
    private val onTextEdit: (String, Int) -> Unit,
    private val onLineBreak: (StoryStep) -> Unit,
    private val onDeleteRequest: (StoryStep) -> Unit
) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(
        step: StoryUnit,
        editable: Boolean,
        focusId: String?,
        extraData: Map<String, Any>
    ) {
        val messageStep = step as StoryStep

        Box(modifier = containerModifier) {
            if (editable) {
                var inputText by remember { mutableStateOf(messageStep.text ?: "") }
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(focusId) {
                    if (focusId == step.id) {
                        focusRequester.requestFocus()
                    }
                }

                BasicTextField(
                    modifier = innerContainerModifier.focusRequester(focusRequester).fillMaxWidth(),
                    value = inputText,
                    onValueChange = { text ->
                        inputText = text


                        when {
                            text.isEmpty() -> { onDeleteRequest(step) }

                            text.contains("\n") -> { onLineBreak(step.copy(text = text)) }

                            else -> { onTextEdit(text, step.localPosition) }
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
