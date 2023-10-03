package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Check item drawer. Draws a check box followed by a text.
 */
class CheckItemDrawer(
    private val modifier: Modifier = Modifier,
    private val onCheckedChange: (Action.StoryStateChange) -> Unit = {},
    private val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    private val emptyErase: (Int) -> Unit = {},
    private val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    private val commandHandler: TextCommandHandler = TextCommandHandler(emptyMap()),
    private val customBackgroundColor: Color? = null,
    private val clickable: Boolean = true,
) : StoryStepDrawer {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val fontSize = 16.sp

        val dropInfo = DropInfo(step, drawInfo.position)
        val focusRequester = remember { FocusRequester() }
        var hasFocus by remember { mutableStateOf(false) }
        var showDragIcon by remember { mutableStateOf(false) }

        var inputText by remember {
            val text = step.text ?: ""
            mutableStateOf(TextFieldValue(text, TextRange(text.length)))
        }

        val textStyle = if (step.checked == true) {
            TextStyle(
                textDecoration = TextDecoration.LineThrough,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = fontSize
            )
        } else {
            TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = fontSize
            )
        }

        LaunchedEffect(drawInfo.focusId) {
            if (drawInfo.focusId == step.id) {
                focusRequester.requestFocus()
            }
        }

        SwipeBox(
            modifier = modifier
                .apply {
                    if (clickable) {
                        clickable {
                            focusRequester.requestFocus()
                        }
                    }
                },
            defaultColor = customBackgroundColor ?: MaterialTheme.colorScheme.background,
            activeColor = MaterialTheme.colorScheme.primary,
            isOnEditState = drawInfo.selectMode,
            swipeListener = { isSelected ->
                onSelected(isSelected, drawInfo.position)
            }
        ) {
            DragTargetWithDragItem(
                dataToDrop = dropInfo,
                showIcon = showDragIcon,
                position = drawInfo.position,
                emptySpaceClick = focusRequester::requestFocus
            ) {
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentEnforcement provides false
                ) {
                    Checkbox(
                        modifier = Modifier.padding(6.dp),
                        checked = step.checked ?: false,
                        onCheckedChange = { checked ->
                            onCheckedChange(
                                Action.StoryStateChange(
                                    step.copy(checked = checked),
                                    drawInfo.position
                                )
                            )
                        },
                        enabled = drawInfo.editable,
                    )
                }

                BasicTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .weight(1F)
                        .onFocusChanged { focusState ->
                            hasFocus = focusState.hasFocus
                        }
                        //Todo: Fix this!!
//                        .callOnEmptyErase(inputText.selection) {
//                            emptyErase(drawInfo.position)
//                        }
                        .onFocusChanged { focusState ->
                            showDragIcon = focusState.hasFocus
                        },
                    value = inputText,
                    onValueChange = { value: TextFieldValue ->
                        if (!commandHandler.handleCommand(
                                value.text,
                                step,
                                drawInfo.position
                            )
                        ) {
                            inputText = value
                            onTextEdit(value.text, drawInfo.position)
                        }
                    },
                    textStyle = textStyle,
                    enabled = drawInfo.editable,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
