package com.storiesteller.sdk.manager

import com.storiesteller.sdk.models.document.Document
import com.storiesteller.sdk.models.story.StoryStep

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
