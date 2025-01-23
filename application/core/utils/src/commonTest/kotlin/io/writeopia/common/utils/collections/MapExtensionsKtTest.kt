package io.writeopia.common.utils.collections

import io.writeopia.common.utils.Node
import kotlin.test.Test
import kotlin.test.assertEquals

class MapExtensionsKtTest {

    @Test
    fun mapsShouldBeMergedCorrectly() {
        val map1 = mapOf(
            "0" to listOf(1, 2, 5),
            "1" to listOf(3, 7)
        )

        val map2 = mapOf(
            "0" to listOf(3, 4),
            "2" to listOf(6, 8)
        )

        val result = map1.merge(map2)
        val expectedMap = mapOf(
            "0" to listOf(1, 2, 5, 3, 4),
            "1" to listOf(3, 7),
            "2" to listOf(6, 8)
        )

        assertEquals(expectedMap, result)
    }

    @Test
    fun nodeTreeShouldBeCorrectlyCreated() {
        val nodes: Map<String, List<SimpleNode>> = mapOf(
            "root" to listOf(
                SimpleNode(id = "0", simpleNodes = mutableListOf()),
                SimpleNode(id = "1", simpleNodes = mutableListOf()),
                SimpleNode(id = "2", simpleNodes = mutableListOf())
            ),
            "0" to listOf(
                SimpleNode(id = "3", simpleNodes = mutableListOf()),
                SimpleNode(id = "4", simpleNodes = mutableListOf()),
            ),
            "4" to listOf(
                SimpleNode(id = "5", simpleNodes = mutableListOf()),
                SimpleNode(id = "6", simpleNodes = mutableListOf()),
            ),
        )

        val expectedNodeTree = SimpleNode(
            id = "root",
            simpleNodes = mutableListOf(
                SimpleNode(
                    id = "0",
                    depth = 0,
                    simpleNodes = mutableListOf(
                        SimpleNode(
                            id = "3",
                            depth = 1,
                            simpleNodes = mutableListOf()
                        ),
                        SimpleNode(
                            id = "4",
                            depth = 1,
                            simpleNodes = mutableListOf(
                                SimpleNode(id = "5", depth = 2, simpleNodes = mutableListOf()),
                                SimpleNode(id = "6", depth = 2, simpleNodes = mutableListOf()),
                            )
                        ),
                    )
                ),
                SimpleNode(id = "1", depth = 0, simpleNodes = mutableListOf()),
                SimpleNode(id = "2", depth = 0, simpleNodes = mutableListOf()),
            )
        )

        val expectedList = expectedNodeTree.toList()
        val result = nodes.toNodeTree(SimpleNode("root", mutableListOf())).toList()

        assertEquals(expectedList, result)
    }
}

data class SimpleNode(
    override val id: String,
    val simpleNodes: MutableList<Node>,
    override var depth: Int = 0
) : Node {

    override fun addNotes(nodes: List<Node>) {
        this.simpleNodes.addAll(nodes)
    }

    override fun getNodes(): List<Node> = this.simpleNodes
}
