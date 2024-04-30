package io.writeopia.sdk.filter

import io.writeopia.sdk.utils.alias.DocumentContent
import io.writeopia.sdk.utils.extensions.associateWithPosition

/**
 * Default implementation of [DocumentFilter]
 */
object DocumentFilterObject : DocumentFilter {

    override fun removeTypesFromDocument(
        documentContent: DocumentContent,
        removeTypes: Set<Int>
    ): DocumentContent =
        documentContent.values.filter { storyStep ->
            !removeTypes.contains(storyStep.type.number)
        }.associateWithPosition()
}
