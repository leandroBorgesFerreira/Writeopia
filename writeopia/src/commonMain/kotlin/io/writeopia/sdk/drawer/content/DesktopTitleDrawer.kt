package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.utils.ui.transparentTextInputColors

const val DESKTOP_TITLE_DRAWER_TEST_TAG = "DesktopTitleDrawerTextField"

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
class DesktopTitleDrawer(
    private val modifier: Modifier = Modifier,
    private val innerPadding: PaddingValues? = null,
    private val onTextEdit: (Action.StoryStateChange) -> Unit = { },
    private val onKeyEvent: (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean = { _, _, _, _ -> false },
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val focusRequester = remember { FocusRequester() }
        val titleStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (drawInfo.editable) {
            val text = step.text ?: ""
            val inputText = TextFieldValue(text, TextRange(text.length))

            LaunchedEffect(drawInfo.focusId) {
                if (drawInfo.focusId == step.id) {
                    focusRequester.requestFocus()
                }
            }

            TextField(
                modifier = modifier
                    .clickable {
                        focusRequester.requestFocus()
                    }
                    .semantics {
                        testTag = "TitleDrawer"
                    }
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onKeyEvent { keyEvent ->
                        onKeyEvent(keyEvent, inputText, step, drawInfo.position)
                    }.let { modifierLet ->
                        innerPadding?.let(modifierLet::padding) ?: modifierLet
                    }
                    .semantics {
                        testTag = DESKTOP_TITLE_DRAWER_TEST_TAG
                    },
                value = inputText,
                onValueChange = { value ->
                    val changedText = value.text
                    if (!changedText.contains("\n")) {
                        onTextEdit(Action.StoryStateChange(step.copy(text = changedText), drawInfo.position))
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                placeholder = {
                    Text(
                        "Title",
//                            stringResource(R.string.title),
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
                modifier = modifier
                    .semantics { testTag = "TitleDrawer" }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
