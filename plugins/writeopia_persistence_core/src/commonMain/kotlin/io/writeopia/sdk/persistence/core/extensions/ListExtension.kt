package io.writeopia.sdk.persistence.core.extensions

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.persistence.core.sorting.OrderBy

fun List<Document>.sortWithOrderBy(orderBy: OrderBy): List<Document> =
    when (orderBy) {
        OrderBy.CREATE -> sortedBy { document -> document.createdAt }
        OrderBy.UPDATE -> sortedBy { document -> document.lastUpdatedAt }
        OrderBy.NAME -> sortedBy { document -> document.title }
    }