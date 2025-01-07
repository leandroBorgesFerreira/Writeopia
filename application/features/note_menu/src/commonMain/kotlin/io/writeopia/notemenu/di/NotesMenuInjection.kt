package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.viewmodel.ChooseNoteViewModel

interface NotesMenuInjection {

    @Composable
    fun provideChooseNoteViewModel(notesNavigation: NotesNavigation): ChooseNoteViewModel
}
