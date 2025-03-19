package io.writeopia.ui.externaldrop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget

@Composable
fun documentFilesDropTarget(
    onFileReceived: (List<String>) -> Unit,
    onStart: () -> Unit,
    onEnd: () -> Unit,
) = remember {
    object : DragAndDropTarget {
        override fun onStarted(event: DragAndDropEvent) {
            onStart()
        }

        override fun onEnded(event: DragAndDropEvent) {
            onEnd()
        }

        override fun onDrop(event: DragAndDropEvent): Boolean {
            val files = event.awtTransferable.let { transferable ->
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    val files =
                        transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                    if (files.isNotEmpty()) {
                        onFileReceived(files.map { file -> file.absolutePath })
                    }

                    files
                } else {
                    emptyList()
                }
            }

            return files.isNotEmpty()
        }
    }
}
