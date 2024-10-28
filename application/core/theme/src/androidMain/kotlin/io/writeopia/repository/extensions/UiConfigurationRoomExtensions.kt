package io.writeopia.repository.extensions

import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import io.writeopia.repository.entity.UiConfigurationEntity

fun UiConfiguration.toRoomEntity() = UiConfigurationEntity(
    userId = userId,
    colorThemeOption = colorThemeOption.theme
)

fun UiConfigurationEntity.toModel() = UiConfiguration(
    userId = userId,
    colorThemeOption = ColorThemeOption.fromText(colorThemeOption) ?: ColorThemeOption.SYSTEM,
    sideMenuWidth = 280F
)
