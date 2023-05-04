package br.com.leandroferreira.storyteller.drawer.content

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.target.DragTarget
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit

class CheckItemDrawer(
    private val onCheckedChange: (String, Boolean) -> Unit,
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
        val checkItem = step as StoryStep

        DragTarget(dataToDrop = checkItem) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Cyan)
                )

                Checkbox(
                    checked = checkItem.checked,
                    onCheckedChange = { checked -> onCheckedChange(checkItem.id, checked) },
                    enabled = editable
                )

                var inputText by remember {
                    val text = checkItem.text ?: ""
                    mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                }

                val textStyle = if (checkItem.checked) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                }

                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(focusId) {
                    if (focusId == step.id) {
                        focusRequester.requestFocus()
                    }
                }

                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL &&
                                inputText.selection.start == 0
                            ) {
                                onDeleteRequest(step)
                                true
                            } else {
                                false
                            }
                        },
                    value = inputText,
                    placeholder = { Text(text = "To-do") },
                    onValueChange = { value ->
                        if (value.text.contains("\n")) {
                            onLineBreak(step.copy(text = value.text))
                        } else {
                            inputText = value
                            onTextEdit(value.text, step.localPosition)
                        }

                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    maxLines = 1,
                    textStyle = textStyle,
                    enabled = editable,
                )
            }
        }
    }
}
