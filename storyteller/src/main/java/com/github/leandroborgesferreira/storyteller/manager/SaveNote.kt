package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.document.Document

interface SaveNote {
    suspend fun saveDocument(document: Document)
}
