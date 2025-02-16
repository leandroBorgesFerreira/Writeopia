package io.writeopia.sdk.export.files

import io.writeopia.sdk.utils.files.KmpClosable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.BufferedWriter
import java.io.File

actual class KmpFileWriter actual constructor(fileName: String) : KmpClosable {

    val file = File(fileName)
    private var writer: BufferedWriter? = null

    actual override fun start() {
        writer = file.bufferedWriter()
    }

    actual fun writeLine(line: String?) {
        writer?.appendLine(line)
    }

    actual override fun close() {
        writer?.close()
    }

    @OptIn(ExperimentalSerializationApi::class)
    actual inline fun <reified T> writeObject(data: T, json: Json) {
        json.encodeToStream(data, file.outputStream())
    }
}
