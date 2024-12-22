package io.writeopia.sdk.models.document

import io.writeopia.sdk.models.utils.Traversable
import kotlinx.datetime.Instant

interface MenuItem : Traversable {
    override val id: String
    val title: String
    val createdAt: Instant
    val lastUpdatedAt: Instant
    val userId: String
    val favorite: Boolean
    val icon: String?
    override val parentId: String
}
