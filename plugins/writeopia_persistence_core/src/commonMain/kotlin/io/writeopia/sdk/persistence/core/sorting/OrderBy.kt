package io.writeopia.sdk.persistence.core.sorting

enum class OrderBy(val type: String) {
    CREATE("create"), UPDATE("update"), NAME(type = "name");

    companion object {
        fun fromString(stringValue: String) = entries.first { orderBy -> orderBy.type == stringValue }
    }

}
