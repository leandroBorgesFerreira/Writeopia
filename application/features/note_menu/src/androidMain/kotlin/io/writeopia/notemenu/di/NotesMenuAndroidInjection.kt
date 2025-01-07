package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.viewmodel.ChooseNoteViewModel
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import kotlinx.coroutines.flow.MutableStateFlow

class NotesMenuAndroidInjection(
    private val notesMenuKmpInjection: NotesMenuKmpInjection,
) : NotesMenuInjection {

    @Composable
    override fun provideChooseNoteViewModel(
        notesNavigation: NotesNavigation
    ): ChooseNoteViewModel = viewModel {
        notesMenuKmpInjection.provideChooseKmpNoteViewModel(notesNavigation = notesNavigation)
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
