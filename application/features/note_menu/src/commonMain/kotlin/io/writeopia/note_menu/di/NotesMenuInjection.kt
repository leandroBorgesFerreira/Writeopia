package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import kotlinx.coroutines.CoroutineScope

interface NotesMenuInjection {

    @Composable
    fun provideChooseNoteViewModel(
        coroutineScope: CoroutineScope?,
        notesNavigation: NotesNavigation
    ): ChooseNoteViewModel

}
