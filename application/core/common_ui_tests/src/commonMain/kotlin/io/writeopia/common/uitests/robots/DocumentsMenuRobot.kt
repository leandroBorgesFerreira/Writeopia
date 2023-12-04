package io.writeopia.common.uitests.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class DocumentsMenuRobot(private val composeTestRule: ComposeTestRule) {

    fun assertNoteWithTitle(title: String) {
        composeTestRule.onNodeWithTag("DocumentItem_$title").assertIsDisplayed()
    }

    fun goToEditNote() {
        composeTestRule.onNodeWithTag("addNote").performClick()
    }
}
