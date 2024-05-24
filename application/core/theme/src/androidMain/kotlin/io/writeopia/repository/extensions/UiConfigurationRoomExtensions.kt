package io.writeopia.repository.extensions

import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import io.writeopia.repository.entity.UiConfigurationRoomEntity

fun UiConfiguration.toRoomEntity() = UiConfigurationRoomEntity(
    userId = userId,
    colorThemeOption = colorThemeOption.theme
)

fun UiConfigurationRoomEntity.toModel() = UiConfiguration(
    userId = userId,
    showSideMenu = false,
    colorThemeOption = ColorThemeOption.fromText(colorThemeOption) ?: ColorThemeOption.SYSTEM
)
