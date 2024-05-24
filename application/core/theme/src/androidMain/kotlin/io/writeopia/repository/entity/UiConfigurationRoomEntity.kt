package io.writeopia.repository.entity

import androidx.room.Entity

@Entity(tableName = "ui_configuration_room")
class UiConfigurationRoomEntity(
    val userId: String,
    val colorThemeOption: String
)
