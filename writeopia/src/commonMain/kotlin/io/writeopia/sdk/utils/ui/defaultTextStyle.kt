package io.writeopia.sdk.utils.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.models.story.StoryStep

@Composable
fun defaultTextStyle(storyStep: StoryStep) =
    TextStyle(
        textDecoration = if (storyStep.checked == true) TextDecoration.LineThrough else null,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp
    )