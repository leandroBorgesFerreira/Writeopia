package io.writeopia.sdk.presentation.model

import io.writeopia.sdk.models.story.StoryStep

data class SlidePage(
    val title: String,
    val subTitle: String? = null,
    val imagePath: String? = null,
    val content: List<StoryStep> = emptyList()
)
