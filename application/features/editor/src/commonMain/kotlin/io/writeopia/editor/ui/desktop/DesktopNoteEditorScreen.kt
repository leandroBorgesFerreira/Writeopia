package io.writeopia.editor.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import io.writeopia.editor.ui.desktop.edit.menu.SideEditorOptions
import io.writeopia.editor.viewmodel.NoteEditorViewModel
import io.writeopia.ui.drawer.factory.DrawersFactory

@Composable
fun DesktopNoteEditorScreen(
    documentId: String?,
    noteEditorViewModel: NoteEditorViewModel,
    drawersFactory: DrawersFactory,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    modifier: Modifier = Modifier,
) {
    Box {
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
            showState = noteEditorViewModel.showTextOptionsMenu,
            fontClick = noteEditorViewModel::toggleOptionsMenu,
            checkItemClick = noteEditorViewModel::onAddCheckListClick,
            listItemClick = noteEditorViewModel::onAddListItemClick,
            codeBlockClick = noteEditorViewModel::onAddCodeBlockClick
        )
    }
}

