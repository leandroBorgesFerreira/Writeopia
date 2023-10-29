package io.writeopia.menu.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.writeopia.editor.NAVIGATE_BACK_TEST_TAG
import io.writeopia.editor.NOTE_EDITION_SCREEN_TITLE_TEST_TAG

class DocumentEditRobot(private val composeTestRule: ComposeTestRule) {

    fun verifyItIsInEdition() {
        composeTestRule.onNodeWithTag(NOTE_EDITION_SCREEN_TITLE_TEST_TAG).assertIsDisplayed()
    }

    fun verifyToolbarTitle(title: String) {
        composeTestRule.onNodeWithTag(NOTE_EDITION_SCREEN_TITLE_TEST_TAG).assertTextContains(title)
    }

    fun writeTitle(text: String) {
        composeTestRule.onNodeWithTag("MobileMessageDrawer_0").performTextInput(text)
    }

    fun goBack() {
        composeTestRule.onNodeWithTag(NAVIGATE_BACK_TEST_TAG).performClick()
    }
}
