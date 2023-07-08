package br.com.leandroferreira.app_sample

import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import br.com.leandroferreira.app_sample.navigation.NavigationActivity

class NoteMenuAndroidTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NavigationActivity>()

    @Test
    fun myTest() {
    }
}