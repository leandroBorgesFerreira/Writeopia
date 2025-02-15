package io.writeopia.persistence.room.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val NOTES_CONFIGURATION = "NOTES_CONFIGURATION"

@Entity(tableName = NOTES_CONFIGURATION)
data class NotesConfigurationEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "arrangement_type") val arrangementType: String,
    @ColumnInfo(name = "order_by_type") val orderByType: String,
    @ColumnInfo(name = "has_tutorials") val hasTutorials: Boolean,
)
