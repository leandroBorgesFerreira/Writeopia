package io.writeopia.utils_module.collections

import io.writeopia.sdk.models.utils.Traversable
import io.writeopia.utils_module.node.Node
import io.writeopia.utils_module.node.createNodeTree

fun <K, V> Map<K, List<V>>.merge(map: Map<K, List<V>>) =
    this.mapValues { (key, dataList) -> dataList + (map[key] ?: emptyList()) }
        .toMutableMap()
        .apply {
            putAll(map.filterKeys { key -> !this.containsKey(key) })
        }

fun <T : Node> Map<String, List<T>>.toNodeTree(node: Node): Node = createNodeTree(this, node)

inline fun <reified T : Traversable, U> List<T>.reverseTraverse(
    initialId: String,
    targetId: String = "root",
    noinline filterPredicate: ((T) -> Boolean)? = null,
    mapFunc: (T) -> U
): List<U> {
    val traversableMap: Map<String, T> = if (filterPredicate != null) {
        this.filter(filterPredicate)
    } else {
        this
    }.associateBy { item -> item.id }

    var currentTraversable: T? = traversableMap[initialId] ?: return emptyList()
    val resultList = mutableListOf(currentTraversable)

    while (currentTraversable != null && currentTraversable.id != targetId) {
        println("iterating. currentTraversable?.id: ${currentTraversable.id}")

        currentTraversable = traversableMap[currentTraversable.parentId]
        resultList.add(currentTraversable)
    }

    return resultList.reversed().filterNotNull().map(mapFunc)
}
