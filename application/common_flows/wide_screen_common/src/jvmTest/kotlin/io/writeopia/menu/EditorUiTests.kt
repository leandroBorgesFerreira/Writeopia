package io.writeopia.menu

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import org.junit.Ignore
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class EditorUiTests {

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun itShouldBePossibleToWriteATitleAndSomeContent() = runComposeUiTest {
        startApp()

        EditorCommonTests.run {
            testAddTitleAndContent()
        }
        EditorCommonTests.run {
            testAddTitleAndContent()
        }
    }

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun itShouldBePossibleToSaveNoteWithTitle() = runComposeUiTest {
        startApp()

        EditorCommonTests.run {
            saveNoteWithTitle()
        }
    }

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun itShouldBePossibleToOpenANoteWithoutTitle() = runComposeUiTest {
        startApp()

        EditorCommonTests.run {
            editNoteLineWithoutTitle()
        }
    }

    @Test
    @Ignore("Library of compose navigation somehow breaks this test. This should be added later")
    fun theBottomBoxShouldInitializeVisible() = runComposeUiTest {
        startApp()

        DocumentsMenuRobot.run {
            goToEditNote()
        }

        onNodeWithTag("EditorBottomContent").assertIsDisplayed()
    }
}
