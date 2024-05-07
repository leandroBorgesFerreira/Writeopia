package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.editor.AndroidNoteEditorViewModel
import io.writeopia.editor.viewmodel.NoteEditorViewModel
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.sdk.persistence.core.di.RepositoryInjector

class EditorInjector internal constructor(
    private val editorKmpInjector: EditorKmpInjector
): TextEditorInjector {

    @Composable
    override fun provideNoteDetailsViewModel(): NoteEditorViewModel =
        viewModel {
            AndroidNoteEditorViewModel(editorKmpInjector.provideNoteEditorViewModel())
        }

    companion object {
        fun create(
            authCoreInjection: AuthCoreInjection,
            daosInjection: RepositoryInjector,
            connectionInjector: ConnectionInjector
        ) = EditorInjector(EditorKmpInjector(authCoreInjection, daosInjection, connectionInjector))
    }
}
