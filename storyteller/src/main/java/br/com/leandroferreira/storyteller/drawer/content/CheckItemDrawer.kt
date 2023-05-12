package br.com.leandroferreira.storyteller.drawer.content

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.target.DragTarget
import br.com.leandroferreira.storyteller.drawer.DrawInfo
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.draganddrop.DropInfo

class CheckItemDrawer(
    private val onCheckedChange: (CheckInfo) -> Unit,
    private val onTextEdit: (String, Int) -> Unit,
    private val onLineBreak: (LineBreakInfo) -> Unit,
    private val onDeleteRequest: (DeleteInfo) -> Unit
) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(step: StoryUnit, drawInfo: DrawInfo) {
        val checkItem = step as StoryStep

        DragTarget(dataToDrop = DropInfo(checkItem, drawInfo.position)) {
            Row(
                modifier = Modifier.padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Cyan)
                )

                Checkbox(
                    checked = checkItem.checked ?: false,
                    onCheckedChange = { checked ->
                        onCheckedChange(
                            CheckInfo(
                                checkItem,
                                drawInfo.position,
                                checked
                            )
                        )
                    },
                    enabled = drawInfo.editable
                )

                var inputText by remember {
                    val text = checkItem.text ?: ""
                    mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                }

                val textStyle = if (checkItem.checked == true) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                }

                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(drawInfo.focusId) {
                    if (drawInfo.focusId == step.id) {
                        focusRequester.requestFocus()
                    }
                }

                TextField(
                    modifier = Modifier
                        .padding(0.dp)
                        .focusRequester(focusRequester)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL &&
                                inputText.selection.start == 0
                            ) {
                                onDeleteRequest(DeleteInfo(step, position = drawInfo.position))
                                true
                            } else {
                                false
                            }
                        },
                    value = inputText,
                    placeholder = { Text(text = "To-do") },
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    maxLines = 1,
                    textStyle = textStyle,
                    enabled = drawInfo.editable,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }
        }
    }
}
