package io.writeopia.sdk.model.document

import io.writeopia.sdk.models.document.Document
import java.time.Instant
import java.util.UUID

/**
 * Dto class to keep information about the document
 */
data class DocumentInfo(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val createdAt: Instant = Instant.now(),
    val lastUpdatedAt: Instant = Instant.now(),
)

fun Document.info(): DocumentInfo = DocumentInfo(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    lastUpdatedAt = this.lastUpdatedAt,
)