package io.writeopia.api.editor_spring

import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.sdk.serialization.data.DocumentApi
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

class EditorHandler(private val writeopiaEditorApi: WriteopiaEditorApi) {

    fun example() = writeopiaEditorApi.example()

    suspend fun saveDocument(documentApi: DocumentApi): ServerResponse =
        ServerResponse.accepted()
            .bodyValueAndAwait(writeopiaEditorApi.saveDocument(documentApi))


    suspend fun getDocument(id: String): ServerResponse =
        writeopiaEditorApi.getDocument(id)?.let { document ->
            ServerResponse.ok().bodyValueAndAwait(document)
        } ?: ServerResponse.notFound().buildAndAwait()

}