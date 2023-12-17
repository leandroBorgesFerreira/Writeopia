package io.writeopia.editor.di

import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.editor.AndroidNoteEditorViewModel
import io.writeopia.sdk.persistence.core.di.RepositoryInjector

class EditorInjector internal constructor(private val editorKmpInjector: EditorKmpInjector) {

    internal fun provideNoteDetailsViewModel(): AndroidNoteEditorViewModel =
        AndroidNoteEditorViewModel(editorKmpInjector.provideNoteEditorViewModel())

    companion object {
        fun create(authCoreInjection: AuthCoreInjection, daosInjection: RepositoryInjector) =
            EditorInjector(EditorKmpInjector(authCoreInjection, daosInjection))
    }
}