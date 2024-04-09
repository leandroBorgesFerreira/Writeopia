package io.writeopia.ui.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.content.HeaderDrawer
import io.writeopia.ui.drawer.content.TextDrawer

@Preview
@Composable
fun HeaderDrawerStepPreview() {
    val step = StoryStep(
        type = StoryTypes.TITLE.type,
        decoration = Decoration(
            backgroundColor = Color.Blue.toArgb(),
            textSize = 16
        ),
        text = "Document Title",
    )

    HeaderDrawer(
        drawer = {
            TextDrawer(
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 16.dp),
                onTextEdit = { },
                onLineBreak = { },
                textStyle = {
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
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
        decoration = Decoration(textSize = 16),
        text = "Document Title",
    )

    HeaderDrawer(
        drawer = {
            TextDrawer(
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 16.dp),
                onTextEdit = { },
                onLineBreak = { },
                textStyle = {
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}