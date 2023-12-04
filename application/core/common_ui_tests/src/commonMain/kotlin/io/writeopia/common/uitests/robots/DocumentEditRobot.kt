package io.writeopia.common.uitests.robots

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import junit.framework.TestCase.assertTrue

class DocumentEditRobot(private val composeTestRule: ComposeTestRule) {

    fun writeTitle(text: String) {
        composeTestRule.onNodeWithTag("MessageDrawer_0").performTextInput(text)
    }

    fun writeText(text: String, index: Int) {
        composeTestRule.onNodeWithTag("MessageDrawer_$index").performTextInput(text)
    }

    fun addLine() {
        composeTestRule.onNodeWithTag("LastEmptySpace").performClick()
    }

    fun clickWithText(text: String) {
        composeTestRule.onNodeWithText(text).performClick()
    }

    fun checkWithText(text: String) {
        assertTrue(composeTestRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty())
    }

    fun checkWithTitle(text: String) {
        composeTestRule.onNodeWithTag("MessageDrawer_0").assertTextContains(text)
    }
}
