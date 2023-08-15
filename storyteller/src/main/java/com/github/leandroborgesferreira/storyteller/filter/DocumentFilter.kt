package com.github.leandroborgesferreira.storyteller.filter

import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.utils.alias.DocumentContent
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition

object DocumentFilter {

    fun removeMetaData(
        documentContent: DocumentContent,
        removeTypes: Set<Int> = removeTypes()
    ): DocumentContent =
        documentContent.values.filter { storyStep ->
            removeTypes.contains(storyStep.type.number)
        }.associateWithPosition()
}

private fun removeTypes(): Set<Int> =
    setOf(
        StoryTypes.SPACE.type.number,
        StoryTypes.LARGE_SPACE.type.number,
    )