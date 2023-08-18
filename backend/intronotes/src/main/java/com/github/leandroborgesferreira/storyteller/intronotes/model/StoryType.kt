package com.github.leandroborgesferreira.storyteller.intronotes.model

import kotlinx.serialization.Serializable

@Serializable
data class StoryType(val name: String, val number: Int)