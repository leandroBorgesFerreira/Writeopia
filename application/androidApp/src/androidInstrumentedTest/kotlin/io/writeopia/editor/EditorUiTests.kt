package io.writeopia.editor

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
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() {
        startContent()

        EditorCommonTests.testAddTitleAndContent(
            DocumentsMenuRobot(composeTestRule),
            DocumentEditRobot(composeTestRule)
        )
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
