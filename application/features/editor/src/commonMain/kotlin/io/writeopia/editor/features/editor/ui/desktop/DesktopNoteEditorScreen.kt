package io.writeopia.editor.features.editor.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import io.writeopia.editor.features.editor.ui.desktop.edit.menu.SideEditorOptions
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel
import io.writeopia.ui.drawer.factory.DrawersFactory

@Composable
fun DesktopNoteEditorScreen(
    documentId: String?,
    noteEditorViewModel: NoteEditorViewModel,
    drawersFactory: DrawersFactory,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    onPresentationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isEditable by noteEditorViewModel.isEditable.collectAsState()

    Box(
        modifier = Modifier.clickable(
            onClick = noteEditorViewModel::clearSelections,
            interactionSource = interactionSource,
            indication = null
        )
    ) {


        EditorScaffold(
            clickAtBottom = noteEditorViewModel.writeopiaManager::clickAtTheEnd,
            modifier = modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(end = 56.dp),
            content = {
                Box {
                    AppTextEditor(
                        noteEditorViewModel.writeopiaManager,
                        noteEditorViewModel,
                        drawersFactory = drawersFactory,
                        loadNoteId = documentId,
                        modifier = Modifier
                            .onPreviewKeyEvent { keyEvent ->
                                if (isUndoKeyEvent(keyEvent)) {
                                    noteEditorViewModel.undo()
                                    true
                                } else {
                                    false
                                }
                            }.padding(start = 30.dp, end = 30.dp)
                    )
                }
            }
        )

        SideEditorOptions(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 40.dp)
                .align(Alignment.TopEnd),
            fontStyleSelected = { noteEditorViewModel.fontFamily },
            isEditableState = noteEditorViewModel.isEditable,
            setEditable = noteEditorViewModel::toggleEditable,
            checkItemClick = noteEditorViewModel::onAddCheckListClick,
            listItemClick = noteEditorViewModel::onAddListItemClick,
            codeBlockClick = noteEditorViewModel::onAddCodeBlockClick,
            highLightBlockClick = noteEditorViewModel::onAddHighLightBlockClick,
            onPresentationClick = onPresentationClick,
            changeFontFamily = noteEditorViewModel::changeFontFamily
        )

        if (!isEditable) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Lock",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                    .size(16.dp)
            )
        }
    }
}
