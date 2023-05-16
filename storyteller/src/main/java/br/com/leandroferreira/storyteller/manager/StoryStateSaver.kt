package br.com.leandroferreira.storyteller.manager

import br.com.leandroferreira.storyteller.model.story.StoryUnit

interface StoryStateSaver {

    suspend fun saveState(documentId: String, content: Map<Int, StoryUnit>)
}
