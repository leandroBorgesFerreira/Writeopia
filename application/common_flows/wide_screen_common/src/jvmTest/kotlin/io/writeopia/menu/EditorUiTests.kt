package io.writeopia.menu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.DatabaseFactory.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.drawer.factory.DefaultDrawersDesktop
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() = runTest {
        startApp(composeTestRule)

        EditorCommonTests.testAddTitleAndContent(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun itShouldBePossibleToSaveNoteWithTitle() = runTest {
        startApp(composeTestRule)

        EditorCommonTests.saveNoteWithTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun itShouldBePossibleToOpenANoteWithoutTitle() = runTest {
        startApp(composeTestRule)

        EditorCommonTests.editNoteLineWithoutTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun theBottomBoxShouldInitializeVisible() = runTest {
        startApp(composeTestRule)

        DocumentsMenuRobot(composeTestRule).goToEditNote()
        composeTestRule.onNodeWithTag("EditorBottomContent").assertIsDisplayed()
    }
}