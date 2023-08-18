package com.github.leandroborgesferreira.storyteller.intronotes.model

import kotlinx.serialization.Serializable

@Serializable
data class StoryStep(
    val id: String,
    val type: StoryType,
    val parentId: String? = null,
    val url: String? = "",
    val path: String? = "",
    val text: String? = "",
    val title: String? = "",
    val checked: Boolean? = false,
    val steps: List<StoryStep> = emptyList(),
    val decoration: Decoration = Decoration(),
    val position: Int,
)