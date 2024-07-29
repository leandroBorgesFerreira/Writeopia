package io.writeopia.note_menu.utils

import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.persistence.core.sorting.OrderBy

fun List<MenuItem>.sortedWithOrderBy(orderBy: OrderBy): List<MenuItem> =
    when (orderBy) {
        OrderBy.CREATE -> this.sortedBy { menuItem -> menuItem.createdAt }
        OrderBy.UPDATE -> this.sortedBy { menuItem -> menuItem.lastUpdatedAt }
        OrderBy.NAME -> this.sortedBy { menuItem -> menuItem.title }
    }
