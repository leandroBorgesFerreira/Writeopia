package io.writeopia.repository

import io.writeopia.model.ColorThemeOption
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object UiConfigurationRepositoryCommonTest {

    suspend fun shouldBePossibleToSaveAndGetColorTheme(repository: UiConfigurationRepository) {
        val userId = "user_offline"
        repository.run {
            val colorTheme = ColorThemeOption.DARK

            this.updateColorTheme(userId, colorTheme)

            assertEquals(
                colorTheme,
                this.getUiConfigurationEntity(userId)!!.colorThemeOption
            )
        }
    }

    suspend fun shouldBePossibleToKeepChoiceToHideSideMenu(repository: UiConfigurationRepository) {
        val userId = "user_offline"
        repository.run {
            updateShowSideMenu(userId, true)
            assertTrue(getUiConfigurationEntity(userId)?.showSideMenu == true)
        }
    }
}
