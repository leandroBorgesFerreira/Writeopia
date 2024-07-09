package io.writeopia.utils_module.collections

fun <K, V> Map<K, List<V>>.merge(map: Map<K, List<V>>) =
    this.mapValues { (key, dataList) -> dataList + (map[key] ?: emptyList()) }
        .toMutableMap()
        .apply {
            putAll(map.filterKeys { key -> !this.containsKey(key) })
        }
