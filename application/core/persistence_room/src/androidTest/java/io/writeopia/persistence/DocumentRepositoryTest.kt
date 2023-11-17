package io.writeopia.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.persistence.dao.DocumentEntityDao
import io.writeopia.sdk.persistence.dao.StoryUnitEntityDao
import io.writeopia.sdk.persistence.dao.room.RoomDocumentRepository
import io.writeopia.sdk.persistence.parse.toEntity
import io.writeopia.sdk.persistence.parse.toModel
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DocumentRepositoryTest {

    private lateinit var database: WriteopiaApplicationDatabase
    private lateinit var documentEntityDao: DocumentEntityDao
    private lateinit var storyUnitEntityDao: StoryUnitEntityDao
    private lateinit var documentRepository: RoomDocumentRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            WriteopiaApplicationDatabase::class.java
        ).build()

        documentEntityDao = database.documentDao()
        storyUnitEntityDao = database.storyUnitDao()

        documentRepository = RoomDocumentRepository(documentEntityDao, storyUnitEntityDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveAndLoadASimpleDocument() = runTest {
        val id = GenerateId.generate()
        val document = Document(
            id = id,
            title = "Document1",
            content = emptyMap(),
            createdAt = Clock.System.now(),
            lastUpdatedAt = Clock.System.now(),
            userId = "userId",
        )

        val loadedDocument = documentEntityDao.run {
            insertDocuments(document.toEntity())
            loadDocumentById(id)
        }

        assertEquals(document, loadedDocument?.toModel())
    }

    @Test
    fun saveSimpleDocumentInRepository() = runTest {
        val id = GenerateId.generate()
        val document = Document(
            id = id,
            title = "Document1",
            content = emptyMap(),
            createdAt = Clock.System.now(),
            lastUpdatedAt = Clock.System.now(),
            userId = "userId",
        )

        documentRepository.saveDocument(document)

        val loadedDocument = documentEntityDao.loadDocumentById(id)
        assertEquals(document, loadedDocument?.toModel())
    }

    @Test
    fun savingAndLoadingDocumentWithOneImageInRepository() = runTest {
        val id = GenerateId.generate()
        val document = Document(
            id = id,
            title = "Document1",
            content = simpleImage(),
            createdAt = Clock.System.now(),
            lastUpdatedAt = Clock.System.now(),
            userId = "userId",
        )

        documentRepository.saveDocument(document)
        val loadedDocument = documentRepository.loadDocumentById(id)

        assertEquals(document, loadedDocument)
    }

    @Test
    fun savingAndLoadingDocumentWithManyImagesInRepository() = runTest {
        val id = GenerateId.generate()
        val document = Document(
            id = id,
            title = "Document1",
            content = imageStepsList(),
            createdAt = Clock.System.now(),
            lastUpdatedAt = Clock.System.now(),
            userId = "userId",
        )

        documentRepository.saveDocument(document)
        val loadedDocument = documentRepository.loadDocumentById(id)

        assertTrue(storyUnitEntityDao.loadDocumentContent(id).isNotEmpty())
        assertTrue(documentEntityDao.loadDocumentWithContentById(id)?.values?.isNotEmpty() ?: false)
        assertEquals(document, loadedDocument)
    }


    @Test
    fun savingAndLoadingDocumentOneImageGroupInRepository() = runTest {
        val id = GenerateId.generate()
        val document = Document(
            id = id,
            title = "Document1",
            content = imageGroup(),
            createdAt = Clock.System.now(),
            lastUpdatedAt = Clock.System.now(),
            userId = "userId",
        )

        documentRepository.saveDocument(document)
        val loadedDocument = documentRepository.loadDocumentById(id)

        assertEquals(document, loadedDocument)
    }
}

fun simpleImage(): Map<Int, StoryStep> = mapOf(
    0 to StoryStep(
        localId = "1",
        type = StoryTypes.IMAGE.type,
    )
)

fun imageStepsList(): Map<Int, StoryStep> = mapOf(
    0 to StoryStep(
        localId = "1",
        type = StoryTypes.IMAGE.type,
    ),
    1 to StoryStep(
        localId = "2",
        type = StoryTypes.IMAGE.type,
    ),
    2 to StoryStep(
        localId = "3",
        type = StoryTypes.IMAGE.type,
    ),
)

fun imageGroup() : Map<Int, StoryStep>{
    val groupId = GenerateId.generate()

    return mapOf(
        0 to StoryStep(
            id = groupId,
            localId = "1",
            type = StoryTypes.GROUP_IMAGE.type,
            steps = listOf(
                StoryStep(
                    localId = "2",
                    type = StoryTypes.IMAGE.type,
                    parentId = groupId,
                ),
                StoryStep(
                    localId = "3",
                    type = StoryTypes.IMAGE.type,
                    parentId = groupId,
                ),
                StoryStep(
                    localId = "4",
                    type = StoryTypes.IMAGE.type,
                    parentId = groupId,
                )
            )
        ),
    )

}
