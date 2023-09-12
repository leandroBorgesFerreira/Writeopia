package com.github.leandroborgesferreira.storytellerapp

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.leandroborgesferreira.storytellerapp.navigation.NavigationGraph
import com.github.leandroborgesferreira.storytellerapp.robots.DocumentEditRobot
import com.github.leandroborgesferreira.storytellerapp.robots.DocumentsMenuRobot
import com.storiesteller.sdk.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations
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
                database = StoryTellerDatabase.database(
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