package io.writeopia.features.search.repository

import io.writeopia.features.search.extensions.toModel
import io.writeopia.notemenu.data.model.Folder
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sqldelight.dao.FolderSqlDelightDao

class SearchRepository(
    private val folderDao: FolderSqlDelightDao,
    private val documentSqlDao: DocumentSqlDao
) {

    fun getNotesAndFolders(): List<SearchItem> {
        val folders = folderDao.getLastUpdated().map { folderEntity -> folderEntity.toModel(0) }
        val documents = documentSqlDao.getLastUpdatedAt()

        return (folders + documents).toSearchItems()
    }

    fun searchNotesAndFolders(query: String): List<SearchItem> {
        val folders = folderDao.search(query).map { folderEntity -> folderEntity.toModel(0) }
        val documents = documentSqlDao.search(query)

        return (folders + documents).toSearchItems()
    }
}

fun List<MenuItem>.toSearchItems(): List<SearchItem> =
    this.map { menuItem ->
        when (menuItem) {
            is Folder -> SearchItem.FolderInfo(menuItem.title)

            else -> SearchItem.DocumentInfo(menuItem.title)
        }
    }.take(12)

sealed interface SearchItem {
    data class FolderInfo(val label: String): SearchItem
    data class DocumentInfo(val label: String): SearchItem
}
