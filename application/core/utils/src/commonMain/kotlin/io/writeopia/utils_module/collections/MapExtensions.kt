package io.writeopia.utils_module.collections

import io.writeopia.utils_module.node.Node
import io.writeopia.utils_module.node.createNodeTree

fun <K, V> Map<K, List<V>>.merge(map: Map<K, List<V>>) =
    this.mapValues { (key, dataList) -> dataList + (map[key] ?: emptyList()) }
        .toMutableMap()
        .apply {
            putAll(map.filterKeys { key -> !this.containsKey(key) })
        }

fun <T : Node> Map<String, List<T>>.toNodeTree(node: Node): Node = createNodeTree(this, node)
