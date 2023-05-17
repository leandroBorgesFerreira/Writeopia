package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.R
import br.com.leandroferreira.storyteller.draganddrop.target.DragTarget
import br.com.leandroferreira.storyteller.drawer.DrawInfo
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.drawer.modifier.callOnEmptyErase
import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.draganddrop.DropInfo
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit

class CheckItemDrawer(
    private val onCheckedChange: (CheckInfo) -> Unit,
    private val onTextEdit: (String, Int) -> Unit,
    private val onLineBreak: (LineBreakInfo) -> Unit,
    private val onDeleteRequest: (DeleteInfo) -> Unit
) : StoryUnitDrawer {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun LazyItemScope.Step(step: StoryUnit, drawInfo: DrawInfo) {
        val checkItem = step as StoryStep

        DragTarget(dataToDrop = DropInfo(checkItem, drawInfo.position)) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val todoPlaceHolder = stringResource(R.string.to_do)

                var inputText by remember {
                    val text = checkItem.text
                        .takeIf { text -> text?.isNotEmpty() == true }
                        ?: todoPlaceHolder
                    mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                }
                val focusRequester = remember { FocusRequester() }

                val textStyle = if (checkItem.checked == true) {
                    TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colors.onBackground
                    )
                } else {
                    TextStyle(color = MaterialTheme.colors.onBackground)
                }

                LaunchedEffect(drawInfo.focusId) {
                    if (drawInfo.focusId == step.id) {
                        focusRequester.requestFocus()
                    }
                }

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Cyan)
                )

                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    Checkbox(
                        modifier = Modifier.padding(6.dp),
                        checked = checkItem.checked ?: false,
                        onCheckedChange = { checked ->
                            onCheckedChange(CheckInfo(checkItem, drawInfo.position, checked))
                        },
                        enabled = drawInfo.editable
                    )
                }

                BasicTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .callOnEmptyErase(inputText.selection) {
                            onDeleteRequest(DeleteInfo(step, drawInfo.position))
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
