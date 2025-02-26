package io.writeopia.common.uitests.tests.editor

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot.goToEditNote

@OptIn(ExperimentalTestApi::class)
object EditorCommonTests {

    fun ComposeUiTest.testAddTitleAndContent() {
        val title = "Some Title"
        val text = "Some text"

        DocumentsMenuRobot.run {
            goToEditNote()
        }

        DocumentEditRobot.run {
            writeTitle(title)
            checkWithTitle(title)
            addLine()
            writeText(text, 1)
            checkWithText(text)
        }
    }

    fun ComposeUiTest.saveNoteWithTitle() {
        val noteTitle = "Note1"

        DocumentsMenuRobot.run {
            goToEditNote()
        }

        DocumentEditRobot.run {
            writeTitle(noteTitle)
            goBack()
        }

        DocumentsMenuRobot.run {
            assertNoteWithTitle(noteTitle)
        }
    }

    fun ComposeUiTest.editNoteLineWithoutTitle() {
        val text = "Text"

        DocumentsMenuRobot.run {
            goToEditNote()
        }

        DocumentEditRobot.run {
            addLine()
            writeText(text, 1)
            goBack()
        }

        DocumentEditRobot.run {
            clickWithText(text)
            checkWithText(text) // It shouldn't crash
        }
    }
}
