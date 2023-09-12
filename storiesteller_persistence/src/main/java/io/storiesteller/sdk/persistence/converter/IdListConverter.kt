package io.storiesteller.sdk.persistence.converter

import androidx.room.TypeConverter

private const val STRING_SEPARATOR = ","
class IdListConverter {

        @TypeConverter
        fun List<String>.toStringData() = this.joinToString(STRING_SEPARATOR)

        @TypeConverter
        fun String.toList() = this.split(STRING_SEPARATOR)

}
