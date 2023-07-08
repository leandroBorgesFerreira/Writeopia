package br.com.leandroferreira.app_sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.leandroferreira.app_sample.navigation.NavigationGraph
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class NoteMenuAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        composeTestRule.setContent {
            NavigationGraph()
        }

        composeTestRule.onNodeWithTag("addNote").performClick()

        composeTestRule.onNodeWithTag("noteEditionScreenTitle").assertIsDisplayed()
    }
}