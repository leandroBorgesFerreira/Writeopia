package io.writeopia.common.utils

interface Node {
    val id: String
    var depth: Int

    fun addNotes(nodes: List<Node>)

    fun getNodes(): List<Node>

    fun toList(): List<Node> =
        listOf(this) + this.getNodes().flatMap { internalNodes -> internalNodes.toList() }
}

/**
 * Creates a node tree of Node<T>. The head of the tree should be provided.
 */
internal fun <T : Node> createNodeTree(
    map: Map<String, List<T>>,
    node: T,
    depth: Int = -1,
    filterPredicate: (T) -> Boolean = { true }
): T {
    val nextNodes = map[node.id]

    nextNodes.takeIf { it?.isNotEmpty() == true }
        ?.map { nextNode ->
            nextNode.apply {
                this.depth = depth + 1
            }
        }?.let(node::addNotes)

    nextNodes?.filter(filterPredicate)
        ?.forEach { nextCode -> createNodeTree(map, nextCode, depth + 1) }

    return node
}
