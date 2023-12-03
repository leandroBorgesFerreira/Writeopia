package io.writeopia.menu

import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.CommonTests
import io.writeopia.common.uitests.DocumentEditRobot
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.driver.DriverFactory
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val compose = createComposeRule()

    @Test
    @Ignore
    fun itShouldBePossibleToWriteATitleAndSomeContent() {
        compose.setContent {
            App(DriverFactory())
        }

        CommonTests.testAddTitleAndContent(DocumentEditRobot(compose))
    }
}