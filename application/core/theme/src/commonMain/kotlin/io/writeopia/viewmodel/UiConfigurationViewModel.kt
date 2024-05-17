package io.writeopia.viewmodel

import io.writeopia.model.ColorThemeOption
import kotlinx.coroutines.flow.StateFlow

interface UiConfigurationViewModel {

    fun listenForColorTheme(getUserId: suspend () -> String): StateFlow<ColorThemeOption?>

    fun changeColorTheme(colorThemeOption: ColorThemeOption)
}
