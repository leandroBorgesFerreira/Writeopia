package io.writeopia.notes

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.DocumentEditRobot
import io.writeopia.robots.DocumentEditPageRobot
import io.writeopia.navigation.NavigationGraph
import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.robots.DocumentsMenuRobot
import io.writeopia.utils_module.Destinations
import org.junit.Rule
import org.junit.Test

class NoteMenuAndroidTest {

    //Todo: Move those tests to a common module once desktopApp also is a viable app.

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToAddNote() {
        startContent()

        DocumentsMenuRobot(composeTestRule).goToEditNote()
        DocumentEditPageRobot(composeTestRule).verifyItIsInEdition()
    }

    @Test
    fun itShouldBePossibleToSaveNoteWithTitle() {
        startContent()

        val noteTitle = "Note1"

        val documentsMenuRobot = DocumentsMenuRobot(composeTestRule)
        documentsMenuRobot.goToEditNote()

        val documentEditPageRobot = DocumentEditPageRobot(composeTestRule)
        documentEditPageRobot.verifyItIsInEdition()

        DocumentEditRobot(composeTestRule).run {
            writeTitle(noteTitle)
        }

        documentEditPageRobot.goBack()
        documentsMenuRobot.assertNoteWithTitle(noteTitle)
    }

    @Test
    fun whenAddingTitleItShouldUpdateToolbarTitleToo() {
        startContent()

        val noteTitle = "Note1"

        val documentsMenuRobot = DocumentsMenuRobot(composeTestRule)
        documentsMenuRobot.goToEditNote()

        val documentEditPageRobot = DocumentEditPageRobot(composeTestRule)
        documentEditPageRobot.verifyItIsInEdition()

        DocumentEditRobot(composeTestRule).run {
            writeTitle(noteTitle)
        }

        documentEditPageRobot.verifyToolbarTitle(noteTitle)
    }

    @Test
    fun itShouldBePossibleToOpenANoteWithoutTitle() {
        startContent()

        val documentsMenuRobot = DocumentsMenuRobot(composeTestRule)
        documentsMenuRobot.goToEditNote()

        val text = "Text"

        val documentEditPageRobot = DocumentEditPageRobot(composeTestRule)
        documentEditPageRobot.verifyItIsInEdition()

        val documentEditRobot = DocumentEditRobot(composeTestRule)
        documentEditRobot.run {
            addLine()
            writeText(text, 2)
        }

        documentEditPageRobot.goBack()

        documentEditRobot.run {
            clickWithText(text)
            checkWithText(text) //It shouldn't crash
        }
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
                startDestination = Destinations.CHOOSE_NOTE.id
            )
        }
    }
}