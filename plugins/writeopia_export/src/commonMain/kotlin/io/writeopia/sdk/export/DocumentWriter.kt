package io.writeopia.sdk.export

import io.writeopia.sdk.models.document.Document

interface DocumentWriter {
    fun writeDocuments(documents: List<Document>, path: String)
}