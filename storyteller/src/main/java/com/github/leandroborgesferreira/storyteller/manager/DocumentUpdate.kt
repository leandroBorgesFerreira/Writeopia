package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

interface DocumentUpdate {
    suspend fun saveDocument(document: Document)

    suspend fun saveDocumentMetadata(document: Document)

    suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String)

    suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String)
}
