package io.writeopia.sdk.export.files

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.utils.files.KmpClosable
import kotlinx.serialization.json.Json

expect class KmpFileWriter(fileName: String) : KmpClosable {

    override fun start()

    fun writeLine(line: String? = null)

    override fun close()

    inline fun <reified T> writeObject(data: T, json: Json)
}

/**
 * Creates the name of the file.
 * @param document [Document] The document
 * @param path String The path of the file
 * @param extension String The extensions, containing dot.
 */
fun name(document: Document, path: String, extension: String) =
    "$path/${document.title.trim().replace(" ", "_")}_${document.id}$extension"