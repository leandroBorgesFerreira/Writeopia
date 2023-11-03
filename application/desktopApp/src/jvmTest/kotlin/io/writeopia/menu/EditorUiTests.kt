package io.writeopia.menu

import androidx.compose.ui.test.junit4.createComposeRule
import io.writeopia.common.uitests.CommonTests
import io.writeopia.common.uitests.DocumentEditRobot
import io.writeopia.notes.desktop.App
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() {
        compose.setContent {
            App()
        }

        CommonTests.testAddTitleAndContent(DocumentEditRobot(compose))
    }
}