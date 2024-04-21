package io.writeopia.sdk.import_document.files

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

actual object KmpFileReader {

    @OptIn(ExperimentalSerializationApi::class)
    actual inline fun <reified T> readObject(filePaths: List<String>, json: Json): Flow<T> =
        flow { }
}