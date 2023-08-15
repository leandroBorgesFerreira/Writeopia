package com.github.leandroborgesferreira.storyteller.filter

import com.github.leandroborgesferreira.storyteller.utils.alias.DocumentContent
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition

object DocumentFilterObject: DocumentFilter {

    override fun removeMetaData(
        documentContent: DocumentContent,
        removeTypes: Set<Int>
    ): DocumentContent =
        documentContent.values.filter { storyStep ->
            removeTypes.contains(storyStep.type.number)
        }.associateWithPosition()
}
