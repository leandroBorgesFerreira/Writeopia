package io.writeopia.common.uitests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule

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
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun checkWithTitle(text: String) {
        composeTestRule.onNodeWithTag("MessageDrawer_0").assertTextContains(text)
    }
}
