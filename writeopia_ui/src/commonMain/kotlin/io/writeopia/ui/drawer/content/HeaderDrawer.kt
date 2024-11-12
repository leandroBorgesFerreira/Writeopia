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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.factory.EndOfText
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawInfo
import io.writeopia.ui.model.EmptyErase
import io.writeopia.ui.utils.transparentTextInputColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        val focusRequester = remember { FocusRequester() }

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
                focusRequester = focusRequester,
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
    onKeyEvent: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, EndOfText) -> Boolean,
    modifier: Modifier = Modifier,
    lineBreakByContent: Boolean,
    selectionState: StateFlow<Boolean>,
): StoryStepDrawer =
    HeaderDrawer(
        modifier = modifier,
        drawer = {
            TextDrawer(
                modifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = manager::handleTextInput,
                onKeyEvent = onKeyEvent,
                lineBreakByContent = lineBreakByContent,
                emptyErase = EmptyErase.DISABLED,
                textStyle = {
                    MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                selectionState = selectionState,
                onSelectionLister = {}
            )
        },
        headerClick = headerClick
    )

@Preview
@Composable
private fun HeaderDrawerStepPreview() {
    val step = sampleStoryStep()

    HeaderDrawer(
        drawer = {
            TextDrawer(
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 16.dp),
                onTextEdit = { _, _, _, _ -> },
                textStyle = {
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                selectionState = MutableStateFlow(false),
                onSelectionLister = {}
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}

@Preview
@Composable
private fun HeaderDrawerStepPreviewNoColor() {
    val step = sampleStoryStep()

    HeaderDrawer(
        drawer = {
            TextDrawer(
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 16.dp),
                onTextEdit = { _, _, _, _ -> },
                textStyle = {
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                selectionState = MutableStateFlow(false),
                onSelectionLister = {}
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}

private fun sampleStoryStep() = StoryStep(
    type = StoryTypes.TITLE.type,
    decoration = Decoration(
        backgroundColor = Color.Blue.toArgb(),
    ),
    text = "Document Title",
)

