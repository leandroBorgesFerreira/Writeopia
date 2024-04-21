@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.writeopia.sdk.import_document.files

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

expect object KmpFileReader {

    inline fun <reified T> readObject(filePaths: List<String>, json: Json): Flow<T>

    inline fun <reified T> readDirectory(directoryPath: String, json: Json): Flow<T>
}