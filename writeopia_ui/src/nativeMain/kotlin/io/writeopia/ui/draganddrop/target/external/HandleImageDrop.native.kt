package io.writeopia.ui.draganddrop.target.external

import androidx.compose.ui.draganddrop.DragAndDropEvent

actual fun handleImageDrop(event: DragAndDropEvent, receiveFiles: (List<String>) -> Unit): Boolean =
    false

actual fun shouldAcceptImageDrop(event: DragAndDropEvent): Boolean = false

