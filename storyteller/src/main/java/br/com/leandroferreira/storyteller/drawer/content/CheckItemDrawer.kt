package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import br.com.leandroferreira.storyteller.text.edition.TextCommandHandler

class CheckItemDrawer(
    private val onCheckedChange: (CheckInfo) -> Unit,
    private val onTextEdit: (String, Int) -> Unit,
    private val onDeleteRequest: (DeleteInfo) -> Unit,
    private val commandHandler: TextCommandHandler
) : StoryUnitDrawer {

    @OptIn(ExperimentalMaterial3Api::class)
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
                    val text = checkItem.text ?: ""
                    mutableStateOf(TextFieldValue(text, TextRange(text.length)))
                }
                val focusRequester = remember { FocusRequester() }

                val textStyle = if (checkItem.checked == true) {
                    TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    TextStyle(color = MaterialTheme.colorScheme.onBackground)
                }

                LaunchedEffect(drawInfo.focusId) {
                    if (drawInfo.focusId == step.id) {
                        focusRequester.requestFocus()
                    }
                }

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Cyan)
                )

                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    Checkbox(
                        modifier = Modifier.padding(6.dp),
                        checked = checkItem.checked ?: false,
                        onCheckedChange = { checked ->
                            onCheckedChange(CheckInfo(checkItem, drawInfo.position, checked))
                        },
                        enabled = drawInfo.editable
                    )
                }

                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .callOnEmptyErase(inputText.selection) {
                            onDeleteRequest(DeleteInfo(step, drawInfo.position))
                        },
                    value = inputText,
                    onValueChange = { value: TextFieldValue ->
                        if (!commandHandler.handleCommand(
                                value.text,
                                checkItem,
                                drawInfo.position
                            )
                        ) {
                            inputText = value
                            onTextEdit(value.text, drawInfo.position)
                        }
                    },
                    placeholder = { Text(text = todoPlaceHolder) },
                    textStyle = textStyle,
                    enabled = drawInfo.editable,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                )
            }
        }
    }
}
