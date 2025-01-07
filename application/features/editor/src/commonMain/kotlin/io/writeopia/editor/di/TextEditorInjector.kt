package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel
import io.writeopia.editor.features.presentation.viewmodel.PresentationViewModel

interface TextEditorInjector {

    @Composable
    fun provideNoteDetailsViewModel(parentFolderId: String): NoteEditorViewModel

    @Composable
    fun providePresentationViewModel(): PresentationViewModel
}
