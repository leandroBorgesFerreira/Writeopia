package io.writeopia.menu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun itShouldBePossibleToWriteATitleAndSomeContent() = runTest {
        startApp(composeTestRule, this)

        EditorCommonTests.testAddTitleAndContent(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun itShouldBePossibleToSaveNoteWithTitle() = runTest {
        startApp(composeTestRule, this)

        EditorCommonTests.saveNoteWithTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun itShouldBePossibleToOpenANoteWithoutTitle() = runTest {
        startApp(composeTestRule, this)

        EditorCommonTests.editNoteLineWithoutTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun theBottomBoxShouldInitializeVisible() = runTest {
        startApp(composeTestRule, this)

        DocumentsMenuRobot(composeTestRule).goToEditNote()
        composeTestRule.onNodeWithTag("EditorBottomContent").assertIsDisplayed()
    }
}
