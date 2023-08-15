package com.github.leandroborgesferreira.storyteller.filter

import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.utils.alias.DocumentContent

interface DocumentFilter {

    fun removeMetaData(documentContent: DocumentContent, removeTypes: Set<Int> = removeTypes()): DocumentContent
}

private fun removeTypes(): Set<Int> =
    setOf(
        StoryTypes.SPACE.type.number,
        StoryTypes.LARGE_SPACE.type.number,
    )