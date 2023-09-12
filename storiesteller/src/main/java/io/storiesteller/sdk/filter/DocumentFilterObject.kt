package io.storiesteller.sdk.filter

import io.storiesteller.sdk.utils.alias.DocumentContent
import io.storiesteller.sdk.utils.extensions.associateWithPosition

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
