package io.writeopia.menu

import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class MenuUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    @Ignore("it should be possible to sync the menu even without notes")
    fun `it should be possible to sync the menu even without notes`() = runTest {
        startApp(composeTestRule, this) { database ->
            database.workspaceConfigurationEntityQueries.insert(
                "disconnected_user",
                "./",
                has_first_configuration = 1L
            )
        }

        DocumentsMenuRobot(composeTestRule).syncClick()
    }
}
