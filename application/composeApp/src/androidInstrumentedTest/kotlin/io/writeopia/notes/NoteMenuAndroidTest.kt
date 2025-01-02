package io.writeopia.notes

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import io.writeopia.navigation.NavigationGraph
import io.writeopia.notemenu.navigation.NoteMenuDestiny
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.robots.AndroidDocumentEditPageRobot
import org.junit.Rule
import org.junit.Test

class NoteMenuAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToAddNote() {
        startContent()

        DocumentsMenuRobot(composeTestRule).goToEditNote()
        AndroidDocumentEditPageRobot(composeTestRule).verifyItIsInEdition()
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

        EditorCommonTests.editNoteLineWithoutTitle(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
    }

    @Test
    fun whenAddingTitleItShouldUpdateToolbarTitleToo() {
        startContent()

        val noteTitle = "Note1"

        val documentsMenuRobot = DocumentsMenuRobot(composeTestRule)
        documentsMenuRobot.goToEditNote()

        val documentEditPageRobot = AndroidDocumentEditPageRobot(composeTestRule)
        documentEditPageRobot.verifyItIsInEdition()

        DocumentEditRobot(composeTestRule).run {
            writeTitle(noteTitle)
        }

        documentEditPageRobot.verifyToolbarTitle(noteTitle)
    }

    private fun startContent() {
        composeTestRule.setContent {
            NavigationGraph(
                application = Application(),
                database = WriteopiaApplicationDatabase.database(
                    LocalContext.current,
                    inMemory = true
                ),
                sharedPreferences = LocalContext.current.getSharedPreferences(
                    "MockShared",
                    Context.MODE_PRIVATE
                ),
                startDestination = NoteMenuDestiny.noteMenu()
            )
        }
    }
}
