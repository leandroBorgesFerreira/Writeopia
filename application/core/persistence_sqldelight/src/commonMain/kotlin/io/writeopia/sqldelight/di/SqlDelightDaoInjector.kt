package io.writeopia.sqldelight.di

import io.writeopia.sdk.models.story.TagInfo
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.core.repository.InMemoryDocumentRepository
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentRepository
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sql.WriteopiaDb
import kotlinx.serialization.encodeToString

class SqlDelightDaoInjector(
    private val database: WriteopiaDb?,
    private val tagsSerializer: (Set<TagInfo>) -> String = { writeopiaJson.encodeToString(it) },
    private val tagsDeserializer: (String) -> Set<TagInfo> = { jsonText ->
        writeopiaJson.decodeFromString<Set<TagInfo>>(jsonText)
    },
) : RepositoryInjector {

    private var documentSqlDao: DocumentSqlDao? = null

    private val inMemoryDocumentRepository = InMemoryDocumentRepository()

    private fun provideDocumentSqlDao(): DocumentSqlDao? =
        database?.run {
            documentSqlDao ?: kotlin.run {
                documentSqlDao = DocumentSqlDao(
                    documentEntityQueries,
                    storyStepEntityQueries,
                    tagsSerializer,
                    tagsDeserializer
                )

                documentSqlDao
            }
        }


    override fun provideDocumentRepository(): DocumentRepository =
        provideDocumentSqlDao()?.let(::SqlDelightDocumentRepository)
            ?: inMemoryDocumentRepository

    companion object {
        fun noop() = SqlDelightDaoInjector(null, { "" }, { emptySet() })
    }
}
