package io.writeopia.persistence.room.injection

import io.writeopia.common.utils.persistence.daos.FolderCommonDao
import io.writeopia.common.utils.persistence.daos.NotesConfigurationCommonDao
import io.writeopia.common.utils.persistence.di.AppDaosInjection
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.persistence.room.data.daos.FolderDaoDelegator
import io.writeopia.persistence.room.data.daos.NotesConfigurationRoomDaoDelegator

class AppRoomDaosInjection private constructor(
    private val database: WriteopiaApplicationDatabase
) : AppDaosInjection {

    override fun provideConfigurationDao(): NotesConfigurationCommonDao =
        NotesConfigurationRoomDaoDelegator(database.notesConfigurationDao())

    override fun provideFolderDao(): FolderCommonDao = FolderDaoDelegator(database.folderRoomDao())

    companion object {
        private var instance: AppRoomDaosInjection? = null

        fun singleton(database: WriteopiaApplicationDatabase): AppRoomDaosInjection =
            instance ?: AppRoomDaosInjection(database).also {
                instance = it
            }
    }
}
