package io.writeopia.core.folders.di

import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.core.folders.repository.FolderRepositorySqlDelight
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.FolderSqlDelightDao
import io.writeopia.sqldelight.di.WriteopiaDbInjector

actual class FoldersInjector private constructor(
    private val writeopiaDb: WriteopiaDb?
)  {
    private fun provideFolderSqlDelightDao() = FolderSqlDelightDao(writeopiaDb)

    actual fun provideFoldersRepository(): FolderRepository =
        FolderRepositorySqlDelight(provideFolderSqlDelightDao())

    actual companion object {
        private var instance: FoldersInjector? = null

        actual fun singleton(): FoldersInjector =
            instance ?: FoldersInjector(WriteopiaDbInjector.singleton()?.database)
    }

}
