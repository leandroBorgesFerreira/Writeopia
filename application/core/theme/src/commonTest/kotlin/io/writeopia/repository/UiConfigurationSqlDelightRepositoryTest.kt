package io.writeopia.repository

import io.writeopia.extensions.toModel
import io.writeopia.model.ColorThemeOption
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object UiConfigurationRepositoryCommonTest {

    suspend fun shouldBePossibleToSaveAndGetColorTheme(repository: UiConfigurationRepository) {
        val userId = "userId"
        repository.run {
            val colorTheme = ColorThemeOption.DARK

            this.updateColorTheme(userId, colorTheme)

            assertEquals(
                colorTheme,
                this.getUiConfigurationEntity(userId)!!.toModel().colorThemeOption
            )
        }
    }


    suspend fun shouldBePossibleToKeepChoiceToHideSideMenu(repository: UiConfigurationRepository) {
        val userId = "userId"
        repository.run {
            updateShowSideMenu(userId, true)
            assertTrue(getUiConfigurationEntity(userId)!!.toModel().showSideMenu)
        }
    }

}
