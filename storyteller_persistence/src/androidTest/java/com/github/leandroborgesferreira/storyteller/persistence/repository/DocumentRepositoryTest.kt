package com.github.leandroborgesferreira.storyteller.persistence.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.parse.toEntity
import com.github.leandroborgesferreira.storyteller.persistence.parse.toModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DocumentRepositoryTest {

    private lateinit var database: StoryTellerDatabase
    private lateinit var documentDao: DocumentDao
    private lateinit var storyUnitDao: StoryUnitDao
    private lateinit var documentRepository: DocumentRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            StoryTellerDatabase::class.java
        ).build()

        documentDao = database.documentDao()
        storyUnitDao = database.storyUnitDao()

        documentRepository = DocumentRepository(documentDao, storyUnitDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveAndLoadASimpleDocument() = runTest {
        val id = UUID.randomUUID().toString()
        val document = Document(
            id = id,
            title = "Document1",
            content = emptyMap()
        )

        val loadedDocument = documentDao.run {
            insertDocuments(document.toEntity())
            loadDocumentById(id)
        }

        assertEquals(document, loadedDocument.toModel())
    }

    @Test
    fun saveSimpleDocumentInRepository() = runTest {
        val id = UUID.randomUUID().toString()
        val document = Document(
            id = id,
            title = "Document1",
            content = emptyMap()
        )

        documentRepository.saveDocument(document)

        val loadedDocument = documentDao.loadDocumentById(id)
        assertEquals(document, loadedDocument.toModel())
    }

    @Test
    fun savingAndLoadingDocumentWithOneImageInRepository() = runTest {
        val id = UUID.randomUUID().toString()
        val document = Document(
            id = id,
            title = "Document1",
            content = simpleImage()
        )

        documentRepository.saveDocument(document)
        val loadedDocument = documentRepository.loadDocumentBy(id)

        assertEquals(document, loadedDocument)
    }

    @Test
    fun savingAndLoadingDocumentWithManyImagesInRepository() = runTest {
        val id = UUID.randomUUID().toString()
        val document = Document(
            id = id,
            title = "Document1",
            content = imageStepsList()
        )

        documentRepository.saveDocument(document)
        val loadedDocument = documentRepository.loadDocumentBy(id)

        assertTrue(storyUnitDao.loadDocumentContent(id).isNotEmpty())
        assertTrue(documentDao.loadDocumentWithContent(id)?.values?.isNotEmpty() ?: false)
        assertEquals(document, loadedDocument)
    }


    @Test
    fun savingAndLoadingDocumentOneImageGroupInRepository() = runTest {
        val id = UUID.randomUUID().toString()
        val document = Document(
            id = id,
            title = "Document1",
            content = imageGroup()
        )

        documentRepository.saveDocument(document)
        val loadedDocument = documentRepository.loadDocumentBy(id)

        assertEquals(document, loadedDocument)
    }
}

fun simpleImage(): Map<Int, StoryUnit> = mapOf(
    0 to StoryStep(
        localId = "1",
        type = "image",
    )
)

fun imageStepsList(): Map<Int, StoryUnit> = mapOf(
    0 to StoryStep(
        localId = "1",
        type = "image",
    ),
    1 to StoryStep(
        localId = "2",
        type = "image",
    ),
    2 to StoryStep(
        localId = "3",
        type = "image",
    ),
)

fun imageGroup() : Map<Int, StoryUnit>{
    val groupId = UUID.randomUUID().toString()

    return mapOf(
        0 to StoryStep(
            id = groupId,
            localId = "1",
            type = "group_image",
            steps = listOf(
                StoryStep(
                    localId = "2",
                    type = "image",
                    parentId = groupId,
                ),
                StoryStep(
                    localId = "3",
                    type = "image",
                    parentId = groupId,
                ),
                StoryStep(
                    localId = "4",
                    type = "image",
                    parentId = groupId,
                )
            )
        ),
    )

}
