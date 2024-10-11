package io.writeopia.sdk.persistence.sqldelight

fun Boolean?.toLong(): Long = if (this == true) 1 else 0
