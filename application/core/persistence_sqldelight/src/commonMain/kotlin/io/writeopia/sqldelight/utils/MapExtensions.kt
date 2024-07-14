package io.writeopia.sqldelight.utils

fun <K> Map<K, Long>.sumValues(map: Map<K, Long>) =
    this.mapValues { (key, data) -> data + (map[key] ?: 0) }
        .toMutableMap()
        .apply {
            putAll(map.filterKeys { key -> !this.containsKey(key) })
        }
