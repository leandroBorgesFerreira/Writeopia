package io.writeopia.notes.desktop.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.editor.ui.desktop.AppTextEditor
import io.writeopia.editor.ui.desktop.EditorScaffold
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.note_menu.di.NotesMenuKmpInjection
import io.writeopia.note_menu.ui.desktop.NotesMenu
import io.writeopia.notes.desktop.components.navigation.NavigationPage
import io.writeopia.notes.desktop.components.navigation.NavigationViewModel
import io.writeopia.sdk.drawer.factory.DefaultDrawersDesktop
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.di.SqlDelightDaoInjector

@Composable
internal fun App(database: WriteopiaDb) {
    val authCoreInjection = KmpAuthCoreInjection()
    val repositoryInjection = SqlDelightDaoInjector(database)

    val editorInjector = EditorKmpInjector(
        authCoreInjection = authCoreInjection,
        repositoryInjection = repositoryInjection
    )

    val notesConfigurationInjector = NotesConfigurationInjector(database)

    val notesMenuInjection = NotesMenuKmpInjection(
        notesConfigurationInjector = notesConfigurationInjector,
        authCoreInjection = authCoreInjection,
        repositoryInjection = repositoryInjection,
    )

    val navigationViewModel = NavigationViewModel()

    MaterialTheme {
        val currentPage by navigationViewModel.navigationPage.collectAsState()

        Crossfade(currentPage, animationSpec = tween(durationMillis = 300)) { state ->
            when (state) {
                NavigationPage.NoteMenu -> {
                    val coroutineScope = rememberCoroutineScope()
                    val chooseNoteViewModel = remember {
                        notesMenuInjection.provideChooseNoteViewModel(coroutineScope)
                    }

                    NotesMenu(
                        chooseNoteViewModel,
                        onNewNoteClick = {
                            navigationViewModel.navigateTo(NavigationPage.Editor())
                        },
                        onNoteClick = { id, title ->
                            navigationViewModel.navigateTo(NavigationPage.Editor(id, title))
                        },
                        onDeleteClick = {
                            chooseNoteViewModel.deleteSelectedNotes()
                        }
                    )
                }

                is NavigationPage.Editor -> {
                    val noteEditorViewModel = remember {
                        editorInjector.provideNoteEditorViewModel()
                    }

                    EditorScaffold(
                        clickAtBottom = noteEditorViewModel.writeopiaManager::clickAtTheEnd,
                        onBackClick = {
                            navigationViewModel.navigateTo(NavigationPage.NoteMenu)
                        },
                        content = {
                            AppTextEditor(
                                noteEditorViewModel.writeopiaManager,
                                noteEditorViewModel,
                                DefaultDrawersDesktop,
                                loadNoteId = state.noteId
                            )
                        }
                    )
                }
            }
        }
    }
}