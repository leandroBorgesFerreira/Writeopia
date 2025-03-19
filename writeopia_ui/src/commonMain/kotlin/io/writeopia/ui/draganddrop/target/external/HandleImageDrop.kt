package io.writeopia.ui.draganddrop.target.external

import androidx.compose.ui.draganddrop.DragAndDropEvent

expect fun handleImageDrop(event: DragAndDropEvent, receiveFiles: (List<String>) -> Unit): Boolean

expect fun shouldAcceptImageDrop(event: DragAndDropEvent): Boolean

