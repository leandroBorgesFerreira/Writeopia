package io.writeopia.sdk.export.files

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.utils.files.KmpClosable
import kotlinx.serialization.json.Json

expect class KmpFileWriter(document: Document, path: String, extension: String): KmpClosable {

    override fun start()

    fun writeLine(line: String? = null)

    override fun close()

    inline fun <reified T> writeObject(data: T, json: Json)
}

internal fun name(document: Document, path: String, extension: String) =
    "$path/${document.title.trim().replace(" ", "_")}$extension"