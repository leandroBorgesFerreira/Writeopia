package io.storiesteller

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import io.storiesteller.navigation.NavigationGraph
import io.storiesteller.robots.DocumentEditRobot
import io.storiesteller.robots.DocumentsMenuRobot
import io.storiesteller.sdk.persistence.database.StoriesTellerDatabase
import io.storiesteller.utils_module.Destinations
import org.junit.Rule
import org.junit.Test

class NoteMenuAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToAddNote() {
        startContent()

        DocumentsMenuRobot(composeTestRule).goToAddNote()
        DocumentEditRobot(composeTestRule).verifyItIsInEdition()
    }

    @Test
    fun itShouldBePossibleToSaveNoteWithTitle() {
        startContent()

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

    private fun startContent() {
        composeTestRule.setContent {
            NavigationGraph(
                application = Application(),
                database = StoriesTellerDatabase.database(
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