package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.DrawInfo
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

/**
 * The header for the Document. It applies some stylish to the title of the document.
 */
class HeaderDrawer(
    private val modifier: Modifier = Modifier,
    private val headerClick: () -> Unit,
    private val drawer: BoxScope.() -> StoryStepDrawer,
) : StoryStepDrawer {

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
                        modifierLet.padding(top = 40.dp)
                    }
                }
                .fillMaxWidth()
        ) {
            drawer().Step(step = step, drawInfo = drawInfo)
        }
    }
}

@Preview
@Composable
fun HeaderDrawerStepPreview() {
    val step = StoryStep(
        type = StoryTypes.TITLE.type,
        decoration = Decoration(
            backgroundColor = Color.Blue.toArgb()
        ),
        text = "Document Title",
    )

    HeaderDrawer(
        drawer = {
            TitleDrawer(
                containerModifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = { _, _ -> },
                onLineBreak = {},
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}

@Preview
@Composable
fun HeaderDrawerStepPreviewNoColor() {
    val step = StoryStep(
        type = StoryTypes.TITLE.type,
        decoration = Decoration(),
        text = "Document Title",
    )

    HeaderDrawer(
        drawer = {
            TitleDrawer(
                containerModifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = { _, _ -> },
                onLineBreak = {},
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}