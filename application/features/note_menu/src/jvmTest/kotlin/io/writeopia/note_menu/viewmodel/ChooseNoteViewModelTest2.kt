package io.writeopia.note_menu.viewmodel

import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.note_menu.di.NotesMenuKmpInjection
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test

class ChooseNoteViewModelTest2 {

    private val selectionState = MutableStateFlow(false)

    @Test
    fun `favorite and un favorite should be possible`() = runTest {
        val viewModel = createViewModel(this, selectionState)

        selectionState.value = true

        viewModel.run {
            requestDocuments(true)

            handleNoteTap("id1")
            handleNoteTap("id2")

            favoriteSelectedNotes()
        }
    }
}

private suspend fun createViewModel(
    testScope: TestScope,
    selectionState: StateFlow<Boolean>
): ChooseNoteKmpViewModel {
    val database = DatabaseFactory.createDatabase(DriverFactory())
    val daoInjector = SqlDelightDaoInjector(database)
    val now = Clock.System.now()

    daoInjector.provideDocumentRepository().run {
        repeat(5) { i ->
            saveDocument(
                Document(
                    id = "id$i",
                    title = "Document$i",
                    content = listOf(
                        (0 to StoryStep(
                            type = StoryTypes.TEXT.type,
                            text = "text_$i"
                        ))
                    ).toMap(),
                    createdAt = now,
                    lastUpdatedAt = now,
                    userId = "userId",
                )
            )
        }
    }

    return NotesMenuKmpInjection(
        notesConfigurationInjector = NotesConfigurationInjector(database),
        authCoreInjection = KmpAuthCoreInjection(),
        repositoryInjection = SqlDelightDaoInjector(database),
        uiConfigurationInjector = UiConfigurationInjector(database),
        selectionState = selectionState,
    ).provideChooseKmpNoteViewModel()
        .apply {
            initCoroutine(testScope, testScope.testScheduler)
        }
}
