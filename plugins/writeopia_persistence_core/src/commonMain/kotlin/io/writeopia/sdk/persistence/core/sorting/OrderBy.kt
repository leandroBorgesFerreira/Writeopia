package io.writeopia.sdk.persistence.core.sorting

enum class OrderBy(val type: String) {
    CREATE("create"), UPDATE("update"), NAME(type = "name");
}
