package io.writeopia.common.uitests.robots

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertTrue

object DocumentEditRobot {
    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.writeTitle(text: String) {
        onNodeWithTag("MessageDrawer_0").performTextInput(text)
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.writeText(text: String, index: Int) {
        onNodeWithTag("MessageDrawer_$index").performTextInput(text)
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.addLine() {
        onNodeWithTag("LastEmptySpace").performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.clickWithText(text: String) {
        onNodeWithText(text).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.checkWithText(text: String) {
        assertTrue(onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty())
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.checkWithTitle(text: String) {
        onNodeWithTag("MessageDrawer_0").assertTextContains(text)
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeUiTest.goBack() {
        onNodeWithTag("NoteEditorScreenNavigateBack").performClick()
    }
}
