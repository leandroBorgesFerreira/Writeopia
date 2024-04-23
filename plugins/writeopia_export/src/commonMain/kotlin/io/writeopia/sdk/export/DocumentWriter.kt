package io.writeopia.sdk.export

import io.writeopia.sdk.models.document.Document

interface DocumentWriter {
    fun writeDocuments(documents: List<Document>, path: String, addHashTable: Boolean = true)

    companion object {
        const val HASH_TABLE_FILE_NAME = "hash_table"
    }
}