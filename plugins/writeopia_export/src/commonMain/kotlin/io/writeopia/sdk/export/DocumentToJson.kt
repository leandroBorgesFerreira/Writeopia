package io.writeopia.sdk.export

import io.writeopia.sdk.export.files.KmpFileWriter
import io.writeopia.sdk.export.files.name
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.utils.files.useKmp
import kotlinx.serialization.json.Json

class DocumentToJson(private val json: Json = writeopiaJson) : DocumentWriter {

    override fun writeDocuments(
        documents: List<Document>,
        path: String,
        addHashTable: Boolean
    ) {
        documents.forEach { document ->
            KmpFileWriter(name(document, path, ".json")).useKmp { writer ->
                writer.writeObject(document.toApi(), json)
            }
        }

        if (addHashTable) {
            val hashTable = documents.associateBy { document -> document.id }
                .mapValues { (_, document) ->
                    document.saveHash
                }

            KmpFileWriter("$path/${DocumentWriter.HASH_TABLE_FILE_NAME}.json")
                .useKmp { writer ->
                    writer.writeObject(hashTable, json)
                }
        }
    }
}