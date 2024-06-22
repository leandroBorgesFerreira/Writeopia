package io.writeopia.note_menu.data.model

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.document.MenuItem
import kotlinx.datetime.Instant

data class Folder(
    override val id: String,
    override val parentId: String,
    override val title: String,
    override val createdAt: Instant,
    override val lastUpdatedAt: Instant,
    override val userId: String,
    override val favorite: Boolean,
    val documentList: List<Document> = emptyList(),
) : MenuItem
