package io.writeopia.sdk.export.files

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.serialization.json.writeopiaJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.BufferedWriter
import java.io.File

actual class KmpFileWriter actual constructor(
    document: Document,
    path: String,
    extension: String,
) {

    val file = File(name(document, path, extension))
    private var writer: BufferedWriter? = null

    actual fun start() {
        writer = file.bufferedWriter()
    }

    actual fun writeLine(line: String?) {
        writer?.appendLine(line)
    }

    actual fun close() {
        writer?.close()
    }

    @OptIn(ExperimentalSerializationApi::class)
    actual inline fun <reified T> writeObject(data: T, json: Json) {
        json.encodeToStream(data, file.outputStream())
    }
}
