package io.writeopia.menu

import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import io.writeopia.common.uitests.robots.DocumentEditRobot
import io.writeopia.common.uitests.robots.DocumentsMenuRobot
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.driver.DriverFactory
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() {
        compose.setContent {
            App(DriverFactory())
        }

        DocumentsMenuRobot(compose).goToEditNote()
        EditorCommonTests.testAddTitleAndContent(DocumentEditRobot(compose))
    }
}