package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel
import io.writeopia.editor.features.presentation.viewmodel.PresentationViewModel
import kotlinx.coroutines.CoroutineScope

interface TextEditorInjector {

    @Composable
    fun provideNoteDetailsViewModel(parentFolderId: String): NoteEditorViewModel

    fun providePresentationViewModel(coroutineScope: CoroutineScope): PresentationViewModel
}
