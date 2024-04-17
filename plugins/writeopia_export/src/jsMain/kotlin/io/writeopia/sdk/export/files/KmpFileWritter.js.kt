package io.writeopia.sdk.export.files

import io.writeopia.sdk.models.document.Document
import kotlinx.serialization.json.Json

actual class KmpFileWriter actual constructor(
    document: Document,
    path: String,
    extension: String
) {
    actual fun start() {
    }

    actual fun writeLine(line: String?) {
    }

    actual fun close() {
    }

    actual inline fun <reified T> writeObject(data: T, json: Json) {
    }
}
