package io.writeopia.sdk.export.files

import io.writeopia.sdk.utils.files.KmpClosable
import kotlinx.serialization.json.Json

actual class KmpFileWriter actual constructor(fileName: String) : KmpClosable {
    actual override fun start() {
    }

    actual fun writeLine(line: String?) {
    }

    actual override fun close() {
    }

    actual inline fun <reified T> writeObject(data: T, json: Json) {
    }
}
