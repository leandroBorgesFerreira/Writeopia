package io.writeopia.sdk.import_document.files

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

actual object KmpFileReader {

    @OptIn(ExperimentalSerializationApi::class)
    actual inline fun <reified T> readObject(filePaths: List<String>, json: Json): Flow<T> =
        filePaths.asFlow().map { path ->
            json.decodeFromStream<T>(File(path).inputStream())
        }
}