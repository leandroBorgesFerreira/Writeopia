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
    val nextNodes = map[node.id] ?: throw IllegalStateException("The start node is not in the tree")
    val nextSteps = nextNodes.filter { it.acceptNodes }

    node.addNotes(nextNodes)

    nextSteps.forEach { nextStepNode ->
        createNodeTree(map, nextStepNode)
    }

    return node
}
