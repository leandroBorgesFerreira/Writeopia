package com.github.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.leandroferreira.storyteller.drawer.DrawInfo
import com.github.leandroferreira.storyteller.drawer.StoryUnitDrawer
import com.github.leandroferreira.storyteller.drawer.modifier.callOnEmptyErase
import com.github.leandroferreira.storyteller.model.change.DeleteInfo
import com.github.leandroferreira.storyteller.model.story.StoryStep
import com.github.leandroferreira.storyteller.model.story.StoryUnit
import com.github.leandroferreira.storyteller.text.edition.TextCommandHandler

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class MessageStepDrawer(
    private val containerModifier: Modifier = Modifier,
    private val innerContainerModifier: Modifier = Modifier,
    private val onTextEdit: (String, Int) -> Unit,
    private val onDeleteRequest: (DeleteInfo) -> Unit,
    private val commandHandler: TextCommandHandler,
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
                    if (drawInfo.focusId == step.id) {
                        focusRequester.requestFocus()
                    }
                }

                BasicTextField(
                    modifier = innerContainerModifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .callOnEmptyErase(inputText.selection) {
                            onDeleteRequest(DeleteInfo(step, drawInfo.position))
                        },
                    value = inputText,
                    onValueChange = { value ->
                        if (!commandHandler.handleCommand(
                            value.text,
                            messageStep,
                            drawInfo.position
                        )
                        ) {
                            inputText = value
                            onTextEdit(value.text, drawInfo.position)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground)
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
