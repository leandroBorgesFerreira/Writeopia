package io.writeopia.menu

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.runComposeUiTest
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class MenuUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    @Ignore("it should be possible to sync the menu even without notes")
    fun `it should be possible to sync the menu even without notes`() = runComposeUiTest {
        startApp { database ->
//            database.workspaceConfigurationEntityQueries.insert(
//                "disconnected_user",
//                "./",
//                has_first_configuration = 1L
//            )
        }

        DocumentsMenuRobot.run { syncClick() }
    }
}
