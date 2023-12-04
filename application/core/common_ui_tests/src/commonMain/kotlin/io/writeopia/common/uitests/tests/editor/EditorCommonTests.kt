package io.writeopia.common.uitests.tests.editor

import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot

object EditorCommonTests {

    fun testAddTitleAndContent(documentsMenuRobot: DocumentsMenuRobot, documentEditRobot: DocumentEditRobot) {
        val title = "Some Title"
        val text = "Some text"

        documentsMenuRobot.goToEditNote()

        documentEditRobot.run {
            writeTitle(title)
            checkWithTitle(title)
            addLine()
            writeText(text, 2)
            checkWithText(text)
        }
    }

    fun saveNoteWithTitle(documentsMenuRobot: DocumentsMenuRobot, documentEditRobot: DocumentEditRobot) {
        val noteTitle = "Note1"

        documentsMenuRobot.goToEditNote()

        documentEditRobot.run {
            writeTitle(noteTitle)
            goBack()
        }

        documentsMenuRobot.assertNoteWithTitle(noteTitle)
    }

    fun openNoteWithoutTitle(documentsMenuRobot: DocumentsMenuRobot, documentEditRobot: DocumentEditRobot) {
        val text = "Text"

        documentsMenuRobot.goToEditNote()

        documentEditRobot.run {
            addLine()
            writeText(text, 2)
            goBack()
        }

        documentEditRobot.run {
            clickWithText(text)
            checkWithText(text) //It shouldn't crash
        }
    }
}