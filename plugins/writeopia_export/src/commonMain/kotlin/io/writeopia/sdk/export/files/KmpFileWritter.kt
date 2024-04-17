package io.writeopia.sdk.export.files

import io.writeopia.sdk.models.document.Document
import kotlinx.serialization.json.Json

expect class KmpFileWriter(document: Document, path: String, extension: String) {

    fun start()

    fun writeLine(line: String? = null)

    fun close()

    inline fun <reified T> writeObject(data: T, json: Json)
}

fun KmpFileWriter.useWriter(func: (KmpFileWriter) -> Unit) {
    start()
    func(this)
    close()
}

internal fun name(document: Document, path: String, extension: String) =
    "$path/${document.title.trim().replace(" ", "_")}$extension"