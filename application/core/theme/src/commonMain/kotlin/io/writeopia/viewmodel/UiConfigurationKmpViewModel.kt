package io.writeopia.viewmodel

import io.writeopia.common.utils.KmpViewModel
import io.writeopia.model.ColorThemeOption
import io.writeopia.repository.UiConfigurationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UiConfigurationKmpViewModel(
    private val uiConfigurationSqlDelightRepository: UiConfigurationRepository
) : KmpViewModel(), UiConfigurationViewModel {

    override fun listenForColorTheme(
        getUserId: suspend () -> String
    ): StateFlow<ColorThemeOption?> =
        uiConfigurationSqlDelightRepository.listenForUiConfiguration(getUserId, coroutineScope)
            .map { uiConfiguration ->
                uiConfiguration?.colorThemeOption ?: ColorThemeOption.SYSTEM
            }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    override fun changeColorTheme(colorThemeOption: ColorThemeOption) {
        coroutineScope.launch(Dispatchers.Default) {
            uiConfigurationSqlDelightRepository.updateColorTheme(
                "user_offline",
                colorThemeOption = colorThemeOption
            )
        }
    }
}
