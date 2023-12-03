package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.note_menu.viewmodel.ChooseNoteAndroidViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteKmpViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.sdk.persistence.core.di.RepositoryInjector

class NotesMenuAndroidInjection(
    private val notesMenuKmpInjection: NotesMenuKmpInjection
) {

    @Composable
    internal fun provideChooseNoteViewModel(
        chooseNoteKmpViewModel: ChooseNoteKmpViewModel =
            notesMenuKmpInjection.provideChooseKmpNoteViewModel()
    ): ChooseNoteViewModel = viewModel { ChooseNoteAndroidViewModel(chooseNoteKmpViewModel) }

    companion object {
        fun create(
            notesConfigurationInjector: NotesConfigurationInjector,
            authCoreInjection: AuthCoreInjection,
            repositoryInjection: RepositoryInjector
        ) = NotesMenuAndroidInjection(
            NotesMenuKmpInjection(
                notesConfigurationInjector,
                authCoreInjection,
                repositoryInjection
            )
        )
    }
}
