package io.writeopia.extensions

import io.writeopia.app.sql.UiConfigurationEntity
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration

fun UiConfiguration.toEntity() = UiConfigurationEntity(
    user_id = userId,
    show_side_menu = showSideMenu.toLong(),
    color_theme_option = colorThemeOption.theme
)

fun UiConfigurationEntity.toModel() = UiConfiguration(
    userId = user_id,
    showSideMenu = show_side_menu.toBoolean(),
    colorThemeOption = ColorThemeOption.fromText(color_theme_option) ?: ColorThemeOption.SYSTEM
)

fun Boolean?.toLong(): Long = if (this == true) 1 else 0

fun Long?.toBoolean() = this == 1L
