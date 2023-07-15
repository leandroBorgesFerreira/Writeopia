package com.github.leandroborgesferreira.storyteller.drawer.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

class MessagePreviewDrawer(
    private val modifier: Modifier = Modifier.padding(vertical = 5.dp)
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Text(
            modifier = modifier,
            text = step.text ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}