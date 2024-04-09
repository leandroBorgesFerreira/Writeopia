package io.writeopia.sdk.models.command

import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryType

data class TypeInfo(
    val storyType: StoryType,
    val decoration: Decoration? = null
)