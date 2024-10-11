package io.writeopia.persistence.room.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.writeopia.persistence.room.data.entities.FOLDER_ENTITY
import io.writeopia.persistence.room.data.entities.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderRoomDao {

    @Upsert
    fun upsertFolder(folderEntity: FolderEntity)

    @Query("SELECT * FROM $FOLDER_ENTITY WHERE folder_id = :id")
    fun getFolderById(id: String): FolderEntity?

    @Query("SELECT * FROM $FOLDER_ENTITY WHERE parent_id = :id")
    fun getFolderByParentId(id: String): List<FolderEntity>

    @Query("SELECT * FROM $FOLDER_ENTITY WHERE parent_id = :id")
    fun listenForFolderByParentId(id: String): Flow<List<FolderEntity>>

    @Query("DELETE FROM $FOLDER_ENTITY WHERE folder_id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM $FOLDER_ENTITY WHERE parent_id = :id")
    fun deleteByParentId(id: String): Int
}
