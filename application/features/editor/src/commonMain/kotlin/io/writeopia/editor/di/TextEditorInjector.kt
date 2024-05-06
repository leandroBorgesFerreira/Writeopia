package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import io.writeopia.editor.viewmodel.NoteEditorViewModel

interface TextEditorInjector {

    @Composable
    fun provideNoteDetailsViewModel(): NoteEditorViewModel
}
