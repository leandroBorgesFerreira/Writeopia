package io.writeopia.extensions

import io.writeopia.app.sql.UiConfigurationEntity
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration

fun UiConfiguration.toEntity() = UiConfigurationEntity(
    user_id = userId,
    color_theme_option = colorThemeOption.theme,
    side_menu_width = sideMenuWidth.toLong()
)

fun UiConfigurationEntity.toModel() = UiConfiguration(
    userId = user_id,
    colorThemeOption = ColorThemeOption.fromText(color_theme_option) ?: ColorThemeOption.SYSTEM,
    sideMenuWidth = side_menu_width?.toFloat() ?: 280F
)

fun Boolean?.toLong(): Long = if (this == true) 1 else 0

fun Long?.toBoolean() = this == 1L
