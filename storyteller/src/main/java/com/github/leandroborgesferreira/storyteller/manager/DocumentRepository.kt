package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

//Todo: Add the methods from DocumentRepositoryImpl
interface DocumentRepository {

    suspend fun loadDocuments(orderBy: String): List<Document>

    suspend fun loadDocumentBy(id: String): Document?

    suspend fun saveDocument(document: Document)

    suspend fun save(documentId: String, content: Map<Int, StoryStep>)

    suspend fun deleteDocument(document: Document)
}
