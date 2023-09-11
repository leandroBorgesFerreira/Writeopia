package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.document.DocumentInfo
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import kotlinx.coroutines.flow.Flow

/**
 * Saves the document automatically based of content changes.
 */
interface DocumentTracker {

    /**
     * Saves both the state of the document using [StoryState] and also the meta information with
     * [DocumentInfo]. A flow should be provided that notifies about the changes in the document.
     */
    suspend fun saveOnStoryChanges(
        documentEditionFlow: Flow<Pair<StoryState, DocumentInfo>>,
        userId: String
    )
}
