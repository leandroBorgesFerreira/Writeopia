package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

interface StoryStateSaver {

    suspend fun saveState(documentId: String, content: Map<Int, StoryUnit>)
}
