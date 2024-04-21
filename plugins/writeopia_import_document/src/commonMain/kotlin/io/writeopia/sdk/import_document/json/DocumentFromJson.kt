package io.writeopia.sdk.import_document.json

import io.writeopia.sdk.import_document.files.KmpFileReader
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.extensions.toModel
import io.writeopia.sdk.serialization.json.writeopiaJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DocumentFromJson(
    private val kmpFileReader: KmpFileReader = KmpFileReader,
    private val json: Json = writeopiaJson
) {

    fun readDocuments(files: List<String>): Flow<Document> =
        kmpFileReader.readObject<DocumentApi>(files, json)
            .map { documentApi -> documentApi.toModel() }
}