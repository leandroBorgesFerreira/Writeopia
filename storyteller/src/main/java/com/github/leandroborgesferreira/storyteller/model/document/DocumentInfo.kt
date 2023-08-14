package com.github.leandroborgesferreira.storyteller.model.document

import java.util.Date
import java.util.UUID

/**
 * Dto class to keep information about the document
 */
data class DocumentInfo(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val createdAt: Date = Date(),
    val lastUpdatedAt: Date = Date(),
)

fun Document.info(): DocumentInfo = DocumentInfo(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    lastUpdatedAt = this.lastUpdatedAt,
)