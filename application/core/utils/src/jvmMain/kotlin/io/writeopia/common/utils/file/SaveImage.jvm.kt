package io.writeopia.common.utils.file

import io.writeopia.sdk.models.id.GenerateId
import java.io.File
import java.io.IOException

actual object SaveImage {
    actual fun saveLocally(externalFile: String, folderPath: String): String {
        val folder = File(folderPath)

        val external = File(externalFile)

        if (folder.exists() && folder.isDirectory) {
            val resultImagePath = "$folderPath/${external.name}"

            val resultImage = File(resultImagePath)
            val currentImage = if (external.exists()) {
                File("${external.absolutePath}${GenerateId.generate()}")
            } else {
                external
            }

            return try {
                // Copy the file
                currentImage.copyTo(resultImage, overwrite = true)
                currentImage.absolutePath
            } catch (e: IOException) {
                external.absolutePath
            }
        }

        return external.absolutePath
    }
}
