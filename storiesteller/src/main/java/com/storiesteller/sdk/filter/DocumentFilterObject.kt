package com.storiesteller.sdk.filter

import com.storiesteller.sdk.utils.alias.DocumentContent
import com.storiesteller.sdk.utils.extensions.associateWithPosition

/**
 * Default implementation of [DocumentFilter]
 */
object DocumentFilterObject: DocumentFilter {

    override fun removeTypesFromDocument(
        documentContent: DocumentContent,
        removeTypes: Set<Int>
    ): DocumentContent =
        documentContent.values.filter { storyStep ->
            !removeTypes.contains(storyStep.type.number)
        }.associateWithPosition()
}
