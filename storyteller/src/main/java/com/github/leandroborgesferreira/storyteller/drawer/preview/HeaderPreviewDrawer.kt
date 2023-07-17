package com.github.leandroborgesferreira.storyteller.drawer.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType

class HeaderPreviewDrawer(
    private val modifier: Modifier = Modifier,
    private val style: TextStyle? = null
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    step.decoration.backgroundColor ?: MaterialTheme.colorScheme.primary
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.BottomStart),
                text = step.text ?: "",
                style = style ?: MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

    }
}

@Preview
@Composable
fun HeaderPreviewDrawerPreview() {
    HeaderPreviewDrawer().Step(
        step = StoryStep(type = StoryType.TITLE.type, text = "Some title"),
        drawInfo = DrawInfo()
    )
}