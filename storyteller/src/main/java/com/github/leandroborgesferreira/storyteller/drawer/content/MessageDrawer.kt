package com.github.leandroborgesferreira.storyteller.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.drawer.modifier.callOnEmptyErase
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.text.edition.TextCommandHandler
import com.github.leandroborgesferreira.storyteller.uicomponents.SwipeBox

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class MessageDrawer(
    private val containerModifier: Modifier = Modifier,
    private val innerContainerModifier: Modifier = Modifier,
    private val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    private val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    private val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    private val customBackgroundColor: Color? = null,
    private val clickable: Boolean = true
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val fontSize = 16.sp
        val focusRequester = remember { FocusRequester() }

        Box(modifier = Modifier.padding(horizontal = 6.dp)) {
            SwipeBox(
                modifier = containerModifier
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
                if (drawInfo.editable) {
                    var inputText by remember {
                        val text = step.text ?: ""
                        mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                    }

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
                                onDeleteRequest(Action.DeleteStory(step, drawInfo.position))
                            },
                        value = inputText,
                        onValueChange = { value ->
                            val text = value.text
                            
                            inputText = if (text.contains("\n")) {
                                val newText = text.split("\n", limit = 2)[0]
                                TextFieldValue(newText, TextRange(newText.length))
                            } else {
                                value
                            }

                            if (!commandHandler.handleCommand(text, step, drawInfo.position)) {
                                onTextEdit(value.text, drawInfo.position)
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = fontSize
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
}
