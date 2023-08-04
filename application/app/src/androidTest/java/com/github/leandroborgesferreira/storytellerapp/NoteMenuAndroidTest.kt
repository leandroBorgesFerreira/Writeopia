package com.github.leandroborgesferreira.storytellerapp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.leandroborgesferreira.storytellerapp.navigation.NavigationGraph
import com.github.leandroborgesferreira.storytellerapp.robots.DocumentEditRobot
import com.github.leandroborgesferreira.storytellerapp.robots.DocumentsMenuRobot
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