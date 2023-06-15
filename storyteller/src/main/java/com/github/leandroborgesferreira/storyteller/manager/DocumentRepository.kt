package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

interface DocumentRepository {

    suspend fun save(documentId: String, content: Map<Int, StoryStep>)
}
