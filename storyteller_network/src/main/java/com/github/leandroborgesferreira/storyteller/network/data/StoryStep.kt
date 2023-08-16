package com.github.leandroborgesferreira.storyteller.network.data

import kotlinx.serialization.Serializable

@Serializable
data class StoryStepApi(
    val id: String,
    val localId: String,
    val type: StoryTypeApi,
    val parentId: String?,
    val url: String?,
    val path: String?,
    val text: String?,
    val title: String?,
    val checked: Boolean?,
    val steps: List<StoryStepApi>,
    val decoration: DecorationApi
)