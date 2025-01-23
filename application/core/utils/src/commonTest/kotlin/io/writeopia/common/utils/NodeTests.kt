package io.writeopia.common.utils

import io.writeopia.common.utils.collections.SimpleNode
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NodeTests {

    @Test
    fun isShouldBePossibleToFindInNodeTree() {
        val nodeTree = SimpleNode(
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

        val result = nodeTree.anyNode { simpleNode ->
            simpleNode.id == "6"
        }

        assertTrue { result }
    }

    @Test
    fun whenNoResultsWereFoundItShouldReturnFalse() {
        val nodeTree = SimpleNode(
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

        val result = nodeTree.anyNode { simpleNode ->
            simpleNode.id == "you won't find this id!!"
        }

        assertFalse { result }
    }
}
