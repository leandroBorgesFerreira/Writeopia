package io.writeopia.persistence.room.extensions

import io.writeopia.common.utils.persistence.NotesConfigurationModel
import io.writeopia.persistence.room.data.entities.NotesConfigurationEntity

fun NotesConfigurationEntity.toCommonEntity(): NotesConfigurationModel {
    return NotesConfigurationModel(
        userId = this.userId,
        arrangementType = this.arrangementType,
        orderByType = this.orderByType
    )
}

fun NotesConfigurationModel.toEntity(): NotesConfigurationEntity {
    return NotesConfigurationEntity(
        userId = this.userId,
        arrangementType = this.arrangementType,
        orderByType = this.orderByType
    )
}
