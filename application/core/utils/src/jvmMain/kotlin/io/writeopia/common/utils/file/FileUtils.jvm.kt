package io.writeopia.common.utils.file

import java.io.File

actual object FileUtils {
    actual fun folderExists(filePath: String) =
        File(filePath).run {
            exists() && isDirectory
        }
}
