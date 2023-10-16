package io.writeopia.sdk.filter

import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.utils.alias.DocumentContent

/**
 * This is a filter to remove content that shouldn't be save in a database or should not be sent to
 * an API in order to save as minimum information as possible. Empty space, for example, should
 * be removed from the document, as they don't carry any information.
 */
interface DocumentFilter {

    /**
     * Removes the selected types from the document passed.
     *
     * @param documentContent [DocumentContent] the document that should be filtered.
     * @param removeTypes all the types that should be removed. The number of the type should be used.
     */
    fun removeTypesFromDocument(
        documentContent: DocumentContent,
        removeTypes: Set<Int> = removeTypesFromDocument()
    ): DocumentContent
}

private fun removeTypesFromDocument(): Set<Int> =
    setOf(
        StoryTypes.SPACE.type.number,
        StoryTypes.LAST_SPACE.type.number,
    )