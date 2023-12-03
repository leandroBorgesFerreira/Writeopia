package io.writeopia.editor

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.CommonTests
import io.writeopia.common.uitests.DocumentEditRobot
import io.writeopia.navigation.NavigationGraph
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.robots.DocumentsMenuRobot
import io.writeopia.utils_module.Destinations
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() {
        startContent()

        CommonTests.testAddTitleAndContent(DocumentEditRobot(composeTestRule))
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

         DocumentsMenuRobot(composeTestRule).goToEditNote()
    }
}