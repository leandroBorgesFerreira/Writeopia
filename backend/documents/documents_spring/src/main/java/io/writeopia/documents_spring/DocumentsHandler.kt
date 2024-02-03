package io.writeopia.documents_spring

import io.writeopia.documents_spring.api.DocumentsRepository
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

class DocumentsHandler(private val documentsRepository: DocumentsRepository) {

    suspend fun userDocuments(): ServerResponse =
        ServerResponse.ok().bodyValueAndAwait(documentsRepository.userNotes())
}
