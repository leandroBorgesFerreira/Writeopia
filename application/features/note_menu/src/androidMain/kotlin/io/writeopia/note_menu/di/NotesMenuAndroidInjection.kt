package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.viewmodel.ChooseNoteAndroidViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class NotesMenuAndroidInjection(
    private val notesMenuKmpInjection: NotesMenuKmpInjection,
) : NotesMenuInjection {

    @Composable
    override fun provideChooseNoteViewModel(
        coroutineScope: CoroutineScope?,
        notesNavigation: NotesNavigation
    ): ChooseNoteViewModel = viewModel {
        ChooseNoteAndroidViewModel(
            notesMenuKmpInjection.provideChooseKmpNoteViewModel(notesNavigation = notesNavigation),
        )
    }

    companion object {
        fun create(
            notesInjector: NotesInjector,
            authCoreInjection: AuthCoreInjection,
            repositoryInjection: RepositoryInjector,
        ) = NotesMenuAndroidInjection(
            NotesMenuKmpInjection(
                notesInjector = notesInjector,
                authCoreInjection = authCoreInjection,
                repositoryInjection = repositoryInjection,
                selectionState = MutableStateFlow(false)
            )
        )
    }
}
