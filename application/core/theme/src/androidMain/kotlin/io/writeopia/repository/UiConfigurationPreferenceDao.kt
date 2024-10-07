package io.writeopia.repository

import android.content.SharedPreferences
import io.writeopia.model.ColorThemeOption
import io.writeopia.repository.entity.UiConfigurationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

class UiConfigurationPreferenceDao(
    private val sharedPreferences: SharedPreferences
) : UiConfigurationRoomDao {

    private val themeState = MutableStateFlow(
        UiConfigurationEntity("disconnected_user", ColorThemeOption.LIGHT.theme)
    )

    override suspend fun saveUiConfiguration(configuration: UiConfigurationEntity) {
        sharedPreferences.edit()
            .putString("colorThemeOption", configuration.colorThemeOption)
            .apply()

        themeState.value = configuration
    }

    override suspend fun getConfigurationByUserId(userId: String): UiConfigurationEntity =
        UiConfigurationEntity(
            userId = "disconnected_user",
            sharedPreferences.getString(
                "colorThemeOption",
                ColorThemeOption.LIGHT.theme
            ) ?: ColorThemeOption.LIGHT.theme
        )

    override fun listenForConfigurationByUserId(userId: String): Flow<UiConfigurationEntity?> {
        runBlocking {
            getConfigurationByUserId("disconnected_user").let {
                themeState.value = it
            }
        }

        return themeState.asStateFlow()
    }
}
