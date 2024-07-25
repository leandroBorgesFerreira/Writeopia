package io.writeopia.utils_module.node

interface Node {
    val id: String
    val acceptNodes: Boolean
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
    depth: Int = 0
): T {
    val nextNodes = map[node.id]
    val nextSteps = nextNodes?.filter { it.acceptNodes }

    nextNodes.takeIf { it?.isNotEmpty() == true }
        ?.map { nextNode ->
            nextNode.apply {
                this.depth = depth + 1
            }
        }?.let(node::addNotes)

    nextSteps?.forEach { nextStepNode ->
        createNodeTree(map, nextStepNode, depth + 1)
    }

    return node
}


