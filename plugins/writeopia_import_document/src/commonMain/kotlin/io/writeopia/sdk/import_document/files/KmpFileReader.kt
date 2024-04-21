package io.writeopia.sdk.import_document.files

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

expect object KmpFileReader {

    inline fun <reified T> readObject(filePaths: List<String>, json: Json): Flow<T>
}