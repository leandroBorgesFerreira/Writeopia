package io.writeopia.sdk.persistence.core.extensions

import io.writeopia.sdk.persistence.core.CREATED_AT
import io.writeopia.sdk.persistence.core.LAST_UPDATED_AT
import io.writeopia.sdk.persistence.core.TITLE
import io.writeopia.sdk.persistence.core.sorting.OrderBy


fun String.toEntityField(): String =
    when (this) {
        OrderBy.CREATE.type -> CREATED_AT
        OrderBy.UPDATE.type -> LAST_UPDATED_AT
        OrderBy.NAME.type -> TITLE
        else -> throw IllegalArgumentException("This type of order doesn't exists: $this")
    }
