package io.writeopia.common.utils.extensions

fun Boolean?.toLong(): Long = if (this == true) 1 else 0

fun Long?.toBoolean() = this == 1L
