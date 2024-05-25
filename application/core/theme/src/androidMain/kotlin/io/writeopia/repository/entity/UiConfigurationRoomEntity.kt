package io.writeopia.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ui_configuration_room")
class UiConfigurationRoomEntity(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "color_theme_option") val colorThemeOption: String
)
