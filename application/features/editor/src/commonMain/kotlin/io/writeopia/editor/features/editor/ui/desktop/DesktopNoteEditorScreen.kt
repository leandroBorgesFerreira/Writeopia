package io.writeopia.editor.features.editor.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.icons.WrIcons
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

        Icon(
            imageVector = WrIcons.play,
            contentDescription = "Presentation",
            modifier = Modifier.padding(12.dp)
                .clip(MaterialTheme.shapes.large)
                .align(Alignment.BottomEnd)
                .clickable(onClick = onPresentationClick)
                .size(40.dp)
                .padding(4.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
