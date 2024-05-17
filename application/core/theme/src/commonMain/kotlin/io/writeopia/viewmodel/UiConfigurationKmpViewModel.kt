package io.writeopia.viewmodel

import io.writeopia.model.ColorThemeOption
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.utils_module.KmpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UiConfigurationKmpViewModel(
    private val uiConfigurationRepository: UiConfigurationRepository
) : KmpViewModel(), UiConfigurationViewModel {

    override fun listenForColorTheme(
        getUserId: suspend () -> String
    ): StateFlow<ColorThemeOption?> =
        uiConfigurationRepository.listenForUiConfiguration(getUserId, coroutineScope)
            .map { uiConfiguration ->
                uiConfiguration?.colorThemeOption ?: ColorThemeOption.SYSTEM
            }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    override fun changeColorTheme(colorThemeOption: ColorThemeOption) {
        coroutineScope.launch(Dispatchers.Default) {
            uiConfigurationRepository.updateColorTheme(
                "user_offline",
                colorThemeOption = colorThemeOption
            )
        }
    }
}
