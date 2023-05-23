package com.github.leandroferreira.storyteller.manager

import com.github.leandroferreira.storyteller.model.story.StoryUnit

interface StoryStateSaver {

    suspend fun saveState(documentId: String, content: Map<Int, StoryUnit>)
}
