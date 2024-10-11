package io.writeopia.editor.input

// import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun InputScreen(
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit = {},
    onForwardPress: () -> Unit = {},
    canUndoState: StateFlow<Boolean>,
    canRedoState: StateFlow<Boolean>,
) {
    val canUndo by canUndoState.collectAsState()
    val canRedo by canRedoState.collectAsState()

    val buttonColor = MaterialTheme.colorScheme.onPrimary
    val disabledColor = Color.LightGray

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.align(Alignment.Center)) {
                Icon(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable(onClick = {
                            if (canUndo) {
                                onBackPress()
                            }
                        }),
                    imageVector = Icons.AutoMirrored.Outlined.Undo,
                    contentDescription = "",
//                    stringResource(R.string.undo),
                    tint = if (canUndo) buttonColor else disabledColor
                )
                Spacer(modifier = Modifier.width(15.dp))
                Icon(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable(onClick = {
                            if (canRedo) {
                                onForwardPress()
                            }
                        }),
                    imageVector = Icons.AutoMirrored.Outlined.Redo,
                    contentDescription = "",
//                    stringResource(R.string.redo),
                    tint = if (canRedo) buttonColor else disabledColor
                )
            }
        }
    }
}

// @Preview
// @Composable
// private fun InputComposablePreview() {
//    InputScreen(
//        canUndoState = MutableStateFlow(true),
//        canRedoState = MutableStateFlow(true)
//    )
// }
