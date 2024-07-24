package io.writeopia.utils_module.node

interface Node {
    val id: String
    val acceptNodes: Boolean
    fun addNotes(nodes: List<Node>)
}

/**
 * Creates a node tree of Node<T>. The head of the tree should be provided.
 */
internal fun <T : Node> createNodeTree(
    map: Map<String, List<T>>,
    node: T
): T {
    val nextNodes = map[node.id]
    val nextSteps = nextNodes?.filter { it.acceptNodes }

    nextNodes.takeIf { it?.isNotEmpty() == true }?.let(node::addNotes)

    nextSteps?.forEach { nextStepNode ->
        createNodeTree(map, nextStepNode)
    }

    return node
}
