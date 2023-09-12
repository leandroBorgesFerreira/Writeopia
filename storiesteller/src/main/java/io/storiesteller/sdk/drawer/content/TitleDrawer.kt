package io.storiesteller.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.storiesteller.sdk.R
import io.storiesteller.sdk.drawer.DrawInfo
import io.storiesteller.sdk.drawer.StoryStepDrawer
import io.storiesteller.sdk.model.action.Action
import io.storiesteller.sdk.models.story.StoryStep
import io.storiesteller.sdk.model.story.StoryTypes
import io.storiesteller.sdk.utils.ui.transparentTextInputColors

const val TITLE_DRAWER_TEST_TAG = "TitleDrawerTextField"

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class TitleDrawer(
    private val containerModifier: Modifier = Modifier,
    private val innerContainerModifier: Modifier = Modifier,
    private val onTextEdit: (String, Int) -> Unit,
    private val onLineBreak: (Action.LineBreak) -> Unit,
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val focusRequester = remember { FocusRequester() }
        val titleStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(modifier = containerModifier
            .clickable {
                focusRequester.requestFocus()
            }
            .semantics {
                testTag = "TitleDrawer"
            }) {
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

                TextField(
                    modifier = innerContainerModifier
                        .focusRequester(focusRequester)
                        .semantics {
                            testTag = TITLE_DRAWER_TEST_TAG
                        },
                    value = inputText,
                    onValueChange = { value ->
                        if (value.text.contains("\n")) {
                            onLineBreak(Action.LineBreak(step, drawInfo.position))
                        } else {
                            inputText = value
                            onTextEdit(value.text, drawInfo.position)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    placeholder = {
                        Text(
                            stringResource(R.string.title),
                            style = titleStyle,
                            color = Color.LightGray
                        )
                    },
                    textStyle = titleStyle,
                    colors = transparentTextInputColors(),
                )
            } else {
                Text(
                    text = step.text ?: "",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun TitleDrawerStepPreview() {
    TitleDrawer(
        onTextEdit = { _, _ -> },
        onLineBreak = {}).Step(
        step = StoryStep(
            type = StoryTypes.TITLE.type,
            text = "Some title"
        ), drawInfo = DrawInfo()
    )
}

