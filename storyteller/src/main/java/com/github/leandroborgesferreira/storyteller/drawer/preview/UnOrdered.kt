package com.github.leandroborgesferreira.storyteller.drawer.preview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

class UnOrderedListItemPreviewDrawer(
    private val modifier: Modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp),
    private val textModifier: Modifier = Modifier,
    private val textStyle: @Composable () -> TextStyle = {
        LocalTextStyle.current
    },
) : StoryStepDrawer {
    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "-", style = textStyle())
            TextPreviewDrawer(modifier = textModifier, style = textStyle()).Step(
                step = step,
                drawInfo = drawInfo
            )
        }
    }
}