package io.storiesteller.sdk.manager

import io.storiesteller.sdk.models.document.Document
import io.storiesteller.sdk.models.story.StoryStep

/**
 * Repository responsible for saving the document updates.
 */
interface DocumentUpdate {
    /**
     * Saves the whole document
     */
    suspend fun saveDocument(document: Document)

    /**
     * Saves the meta data of the document. Content is not saved.
     */
    suspend fun saveDocumentMetadata(document: Document)

    /**
     * Saves of [StoryStep] of a document
     */
    suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String)

    /**
     * Updates one [StoryStep] of a document.
     */
    suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String)
}
