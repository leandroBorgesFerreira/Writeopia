package br.com.leandroferreira.app_sample

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import br.com.leandroferreira.app_sample.navigation.NavigationGraph
import br.com.leandroferreira.app_sample.robots.DocumentEditRobot
import br.com.leandroferreira.app_sample.robots.DocumentsMenuRobot
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import org.junit.Rule
import org.junit.Test

class NoteMenuAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToAddNote() {
        composeTestRule.setContent {
            NavigationGraph(
                database = StoryTellerDatabase.database(
                    LocalContext.current,
                    inMemory = true
                )
            )
        }

        DocumentsMenuRobot(composeTestRule).goToAddNote()
        DocumentEditRobot(composeTestRule).verifyItIsInEdition()
    }

    @Test
    fun itShouldBePossibleToSaveNoteWithTitle() {
        composeTestRule.setContent {
            NavigationGraph(
                database = StoryTellerDatabase.database(
                    LocalContext.current,
                    inMemory = true
                )
            )
        }

        val noteTitle = "Note1"

        val documentsMenuRobot = DocumentsMenuRobot(composeTestRule)
        documentsMenuRobot.goToAddNote()

        DocumentEditRobot(composeTestRule).run {
            verifyItIsInEdition()
            writeTitle(noteTitle)
            goBack()
        }

        documentsMenuRobot.assertNoteWithTitle(noteTitle)
    }
}