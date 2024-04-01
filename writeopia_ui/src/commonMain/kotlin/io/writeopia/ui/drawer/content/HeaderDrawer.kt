package io.writeopia.ui.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.utils.transparentTextInputColors

/**
 * The header for the Document. It applies some stylish to the title of the document.
 */
class HeaderDrawer(
    private val modifier: Modifier = Modifier,
    private val headerClick: () -> Unit = {},
    private val drawer: BoxScope.() -> SimpleTextDrawer,
) : StoryStepDrawer {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val backgroundColor = step.decoration.backgroundColor

        Box(
            modifier = modifier
                .clickable(onClick = headerClick)
                .let { modifierLet ->
                    if (backgroundColor != null) {
                        modifierLet
                            .background(Color(backgroundColor))
                            .padding(top = 130.dp)
                    } else {
                        modifierLet.padding(top = 30.dp)
                    }
                }
                .fillMaxWidth()
        ) {
            val interactionSource = remember { MutableInteractionSource() }

            drawer().Text(
                step = step,
                drawInfo = drawInfo,
                interactionSource = interactionSource,
                focusRequester = null,
                decorationBox = @Composable { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = step.text ?: "",
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        placeholder = {
                            Text(
                                text = "Title",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )
                            )
                        },
                        colors = transparentTextInputColors(),
                    )
                },
            )
        }
    }
}

fun headerDrawer(
    manager: WriteopiaStateManager,
    headerClick: () -> Unit,
    onKeyEvent: (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean,
    modifier: Modifier = Modifier,
): StoryStepDrawer =
    HeaderDrawer(
        modifier = modifier,
        drawer = {
            TextDrawer(
                modifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = manager::changeStoryText,
                onKeyEvent = onKeyEvent,
                onLineBreak = manager::onLineBreak,
                textStyle = {
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        headerClick = headerClick
    )
