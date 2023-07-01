package com.github.leandroborgesferreira.storyteller.persistence.sorting

import com.github.leandroborgesferreira.storyteller.persistence.entity.document.CREATED_AT
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.LAST_UPDATED_AT
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.TITLE

enum class OrderBy(val type: String) {
    CREATE("create"), UPDATE("update"), NAME(type = "name");
}

fun String.toEntityField(): String =
    when (this) {
        OrderBy.CREATE.type -> CREATED_AT
        OrderBy.UPDATE.type -> LAST_UPDATED_AT
        OrderBy.NAME.type -> TITLE
        else -> throw IllegalArgumentException("This type of order doesn't exists: $this")
    }
