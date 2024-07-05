package io.writeopia.sdk.models.document

import kotlinx.datetime.Instant

interface MenuItem {
    val id: String
    val title: String
    val createdAt: Instant
    val lastUpdatedAt: Instant
    val userId: String
    val favorite: Boolean
    val parentId: String
}
