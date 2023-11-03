package io.writeopia.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.writeopia.note_menu.ui.screen.menu.ADD_NOTE_TEST_TAG
import io.writeopia.note_menu.ui.screen.menu.DOCUMENT_ITEM_TEST_TAG

class DocumentsMenuRobot(private val composeTestRule: ComposeTestRule) {

    fun assertNoteWithTitle(title: String) {
        composeTestRule.onNodeWithTag("$DOCUMENT_ITEM_TEST_TAG$title").assertIsDisplayed()
    }

    fun goToEditNote() {
        composeTestRule.onNodeWithTag(ADD_NOTE_TEST_TAG).performClick()
    }
}
