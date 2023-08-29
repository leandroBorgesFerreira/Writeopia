package com.github.leandroborgesferreira.storyteller.drawer.content.decoration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.SimpleMessageDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

class ListItemDecoratorDrawer(
    private val modifier: Modifier = Modifier,
    private val textStyle: @Composable () -> TextStyle = {
        LocalTextStyle.current
    },
    private val startText: String = "-",
    private val stepDrawer: StoryStepDrawer
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

            stepDrawer.Step(step = step, drawInfo = drawInfo)
        }
    }
}

@Preview
@Composable
private fun ListItemDecoratorDrawerPreview() {
    val focusRequester = remember { FocusRequester() }

    ListItemDecoratorDrawer(
        modifier = Modifier.background(Color.Cyan),
        stepDrawer = SimpleMessageDrawer(
            focusRequester = focusRequester
        )
    ).Step(
        step = StoryStep(text = "Some text", type = StoryTypes.MESSAGE.type),
        drawInfo = DrawInfo()
    )
}
