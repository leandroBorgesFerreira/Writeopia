package br.com.leandroferreira.storyteller.drawer.content

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.target.DragTarget
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit

class CheckItemDrawer(
    private val onCheckedChange: (String, Boolean) -> Unit,
    private val onTextEdit: (String, Int) -> Unit,
    private val onDeleteCommand: (StoryStep) -> Unit
) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(
        step: StoryUnit,
        editable: Boolean,
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

                var inputText by remember { mutableStateOf(checkItem.text ?: "") }

                val textStyle = if (checkItem.checked) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                }

                TextField(
                    modifier = Modifier,
                    value = inputText,
                    placeholder = { Text(text = "To-do") },
                    onValueChange = { text ->
                        inputText = text

                        if (text.isEmpty()) {
                            onDeleteCommand(step)
                        } else {
                            onTextEdit(text, step.localPosition)
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    textStyle = textStyle,
                    enabled = editable,
                )
            }
        }
    }
}
