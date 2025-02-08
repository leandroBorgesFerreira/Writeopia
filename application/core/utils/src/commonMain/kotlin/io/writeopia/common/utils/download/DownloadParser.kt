package io.writeopia.common.utils.download

object DownloadParser {
    fun toHumanReadableAmount(bytes: Long?): String {
        if (bytes == null) return ""

        if (bytes < 1024) return "$bytes B"

        val units = arrayOf("KB", "MB", "GB", "TB", "PB", "EB")
        var value: Double = bytes.toDouble()
        var unitIndex = -1

        while (value >= 1024 && unitIndex < units.lastIndex) {
            value /= 1024
            unitIndex++
        }

        var acc = 0
        val valueText = value.toString().takeWhile {
            acc++
            acc < 4
        }

        return "$valueText ${units[unitIndex]}"
    }
}
