package com.github.leandroborgesferreira.storyteller.network.data

import kotlinx.serialization.Serializable

@Serializable
data class StoryTypeApi(val name: String, val number: Int)