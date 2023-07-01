package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

//Todo: Add the methods from DocumentRepositoryImpl
interface DocumentRepository {

    suspend fun save(documentId: String, content: Map<Int, StoryStep>)
}
