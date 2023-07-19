package com.github.leandroborgesferreira.storyteller.drawer.content

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.model.story.Decoration
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType

class HeaderDrawer(
    private val modifier: Modifier = Modifier,
    private val titleDrawer: BoxScope.() -> TitleDrawer,
    private val headerClick: () -> Unit,
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
                            .background(backgroundColor)
                            .padding(top = 90.dp)
                    } else {
                        modifierLet.padding(top = 40.dp)
                    }
                }
                .fillMaxWidth()
        ) {
            titleDrawer().Step(step = step, drawInfo = drawInfo)
        }
    }
}

@Preview
@Composable
fun HeaderDrawerStepPreview() {
    val step = StoryStep(
        type = StoryType.TITLE.type,
        decoration = Decoration(backgroundColor = Color.Blue),
        text = "Document Title",
    )

    HeaderDrawer(
        titleDrawer = {
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
        type = StoryType.TITLE.type,
        decoration = Decoration(),
        text = "Document Title",
    )

    HeaderDrawer(
        titleDrawer = {
            TitleDrawer(
                containerModifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = { _, _ -> },
                onLineBreak = {},
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}