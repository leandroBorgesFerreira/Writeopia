package io.writeopia.editor

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.runComposeUiTest
import io.writeopia.common.uitests.tests.editor.EditorCommonTests
import org.junit.Rule
import org.junit.Test

class EditorUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun itShouldBePossibleToWriteATitleAndSomeContent() = runComposeUiTest {
        startContent()

        EditorCommonTests.run {
            testAddTitleAndContent()
        }
    }

    private fun startContent() {
        composeTestRule.setContent {
//            NavigationGraph(
//                application = Application(),
//                database = WriteopiaApplicationDatabase.database(
//                    LocalContext.current,
//                ),
//                sharedPreferences = LocalContext.current.getSharedPreferences(
//                    "MockShared",
//                    Context.MODE_PRIVATE
//                ),
//                startDestination = NoteMenuDestiny.noteMenu()
//            )
        }
    }
}
