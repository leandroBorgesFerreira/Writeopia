package io.writeopia.common.uitests.robots

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

object DocumentsMenuRobot {
    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.assertNoteWithTitle(title: String) {
        onNodeWithTag("DocumentItem_$title").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.goToEditNote() {
        onNodeWithTag("addNote").performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.syncClick() {
        onNodeWithTag("syncWorkspaceLocally").performClick()
    }
}
