package io.writeopia.sdk.model.document

import io.writeopia.sdk.models.document.Document
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*

/**
 * Dto class to keep information about the document
 */
data class DocumentInfo(
    val id: String = UUID.randomUUID().toString(),
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