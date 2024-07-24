package io.writeopia.utils_module.collections

import androidx.compose.ui.util.trace
import io.writeopia.utils_module.node.Node
import io.writeopia.utils_module.node.createNodeTree
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
                SimpleNode(id = "0", nodes = mutableListOf()),
                SimpleNode(id = "1", nodes = mutableListOf()),
                SimpleNode(id = "2", nodes = mutableListOf())
            ),
            "0" to listOf(
                SimpleNode(id = "3", nodes = mutableListOf()),
                SimpleNode(id = "4", nodes = mutableListOf()),
            ),
            "4" to listOf(
                SimpleNode(id = "5", nodes = mutableListOf()),
                SimpleNode(id = "6", nodes = mutableListOf()),
            ),
        )

        val result = nodes.toNodeTree(SimpleNode("root", mutableListOf()))
//        val result = nodes.toNodeTree(SimpleNode("root", mutableListOf()))

        println("ha")
    }
}

class SimpleNode(override val id: String, val nodes: MutableList<Node>) : Node {

    override val acceptNodes: Boolean = true

    override fun addNotes(nodes: List<Node>) {
        this.nodes.addAll(nodes)
    }
}
