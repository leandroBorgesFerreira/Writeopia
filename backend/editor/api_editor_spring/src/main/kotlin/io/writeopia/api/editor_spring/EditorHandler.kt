package io.writeopia.api.editor_spring

import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.api.editor_spring.config.notesApiFromToken
import io.writeopia.sdk.serialization.data.DocumentApi
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

class EditorHandler(private val writeopiaEditorApi: WriteopiaEditorApi) {

    suspend fun example(): ServerResponse =
        ServerResponse.ok().bodyValueAndAwait(writeopiaEditorApi.example())

    suspend fun introNotes(): ServerResponse =
        ServerResponse.accepted().bodyValueAndAwait(writeopiaEditorApi.introNotes())

    suspend fun saveDocument(documentApi: DocumentApi): ServerResponse =
        ServerResponse.accepted()
            .bodyValueAndAwait(writeopiaEditorApi.saveDocument(documentApi))

    suspend fun getDocument(id: String): ServerResponse =
        writeopiaEditorApi.getDocument(id)?.let { document ->
            ServerResponse.ok().bodyValueAndAwait(document)
        } ?: ServerResponse.notFound().buildAndAwait()

    suspend fun getProxyUserDocument(firebaseToken: String): ServerResponse {
        try {
            val documentApiList = notesApiFromToken(firebaseToken).proxyUserDocumentsApi()
            return ServerResponse.ok().bodyValueAndAwait(documentApiList)
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }
}
