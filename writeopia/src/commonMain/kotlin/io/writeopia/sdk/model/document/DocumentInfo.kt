package io.writeopia.sdk.model.document

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.id.GenerateId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Dto class to keep information about the document
 */
data class DocumentInfo(
    val id: String = GenerateId.generate(),
    val title: String = "",
    val createdAt: Instant,
    val lastUpdatedAt: Instant,
) {
    companion object {
        fun empty(): DocumentInfo {
            val now = Clock.System.now()
            return DocumentInfo(
                createdAt = now,
                lastUpdatedAt = now
            )
        }
    }
}

fun Document.info(): DocumentInfo = DocumentInfo(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    lastUpdatedAt = this.lastUpdatedAt,
)