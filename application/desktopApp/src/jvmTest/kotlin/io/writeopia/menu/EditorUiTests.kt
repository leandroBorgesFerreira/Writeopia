package io.writeopia.menu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() {
        startContent()

        EditorCommonTests.testAddTitleAndContent(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun itShouldBePossibleToSaveNoteWithTitle() {
        startContent()

        EditorCommonTests.saveNoteWithTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun itShouldBePossibleToOpenANoteWithoutTitle() {
        startContent()

        EditorCommonTests.openNoteWithoutTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun theBottomBoxShouldInitializeVisible() {
        startContent()

        DocumentsMenuRobot(composeTestRule).goToEditNote()
        composeTestRule.onNodeWithTag("EditorBottomContent").assertIsDisplayed()
    }


    private fun startContent() {
        composeTestRule.setContent {
            App(createDatabase(DriverFactory()))
        }
    }
}