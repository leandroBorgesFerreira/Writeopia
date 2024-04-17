package io.writeopia.sdk.export

import io.writeopia.sdk.export.files.KmpFileWriter
import io.writeopia.sdk.export.files.useWriter
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import kotlinx.serialization.json.Json

class DocumentToJson(private val json: Json = writeopiaJson): DocumentWriter {

    override fun writeDocuments(documents: List<Document>, path: String) {
        documents.forEach { document ->
            KmpFileWriter(document, path, ".json").useWriter { writer ->
                writer.writeObject(document.toApi(), json)
            }
        }
    }
}