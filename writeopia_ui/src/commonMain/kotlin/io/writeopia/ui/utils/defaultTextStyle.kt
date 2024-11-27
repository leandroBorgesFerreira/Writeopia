package io.writeopia.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.Tags

@Composable
fun defaultTextStyle(storyStep: StoryStep) =
    TextStyle(
        textDecoration = if (storyStep.checked == true) TextDecoration.LineThrough else null,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = textSizeFromTags(storyStep.tags)
    )

@Composable
fun codeBlockStyle() =
    TextStyle(
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp,
        fontFamily = FontFamily.Serif
    )

private fun textSizeFromTags(tags: Iterable<String>) =
    tags.asSequence()
        .map(Tags::fromString)
        .firstOrNull { tag -> tag?.isTitle() == true }
        ?.let(::textSizeFromTag)
        ?: 16.sp

private fun textSizeFromTag(tags: Tags) =
    when (tags) {
        Tags.H1 -> 32.sp
        Tags.H2 -> 28.sp
        Tags.H3 -> 24.sp
        Tags.H4 -> 20.sp
    }
