package io.writeopia.common.utils.file

import java.io.File
import java.io.IOException

actual object SaveImage {
    actual fun saveLocally(externalFile: String, folderPath: String): String {
        val folder = File(folderPath)

        val external = File(externalFile)

        if (folder.exists() && folder.isDirectory) {
            val resultImagePath = "$folderPath/${external.name}"

            val resultImage = File(resultImagePath)
            val currentImage = if (resultImage.exists()) {
                File(external.absolutePath)
            } else {
                external
            }

            return try {
                currentImage.copyTo(resultImage, overwrite = true)
                resultImage.absolutePath
            } catch (e: IOException) {
                println("Error: ${e.message}")
                external.absolutePath
            }
        }

        return external.absolutePath
    }
}
