package io.writeopia.note_menu.data.usecase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationRoomRepository
import io.writeopia.note_menu.data.repository.RoomFolderRepository
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.dao.room.RoomDocumentRepository
import io.writeopia.utils_module.DISCONNECTED_USER_ID
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertTrue

class NotesUseCaseIntegrationTest {

    private lateinit var database: WriteopiaApplicationDatabase

    private lateinit var documentRepository: DocumentRepository
    private lateinit var folderRepository: RoomFolderRepository
    private lateinit var notesConfig: ConfigurationRepository

    private lateinit var notesUseCase: NotesUseCase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            WriteopiaApplicationDatabase::class.java
        ).build()

        notesConfig = ConfigurationRoomRepository(database.notesConfigurationDao())

        documentRepository = RoomDocumentRepository(database.documentDao(), database.storyUnitDao())
        folderRepository = RoomFolderRepository(database.folderRoomDao())

        notesUseCase = NotesUseCase.singleton(
            documentRepository,
            notesConfig = notesConfig,
            folderRepository = folderRepository
        )
    }

    @Test
    fun itShouldBePossibleToSaveAndLoadADocument() = runTest {
        val now = Clock.System.now()

        notesUseCase.saveDocument(
            Document(
                id = "documentId",
                title = "Document1",
                createdAt = now,
                lastUpdatedAt = now,
                userId = DISCONNECTED_USER_ID,
                parentId = "root"
            )
        )

        assertTrue {
            notesUseCase.loadDocumentsForUser(DISCONNECTED_USER_ID).isNotEmpty()
        }
    }
}
