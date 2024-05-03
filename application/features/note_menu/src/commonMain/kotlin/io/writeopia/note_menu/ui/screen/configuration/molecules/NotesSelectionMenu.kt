package io.writeopia.note_menu.ui.screen.configuration.molecules

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.writeopia.common_ui.SlideInBox
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun NotesSelectionMenu(
    modifier: Modifier = Modifier,
    visibilityState: Boolean,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onFavorite: () -> Unit,
    shape: Shape = RoundedCornerShape(
        CornerSize(16.dp),
        CornerSize(16.dp),
        CornerSize(0.dp),
        CornerSize(0.dp)
    ),
    exitAnimationOffset: Float = 1F,
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = 4000F,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
) {
    SlideInBox(
        modifier = modifier,
        editState = visibilityState,
        showBackground = false,
        outsideClick = { },
        enterAnimationSpec = animationSpec,
        exitAnimationSpec = animationSpec,
        animationLabel = "configurationsMenuAnimation",
        extraExitOffset = exitAnimationOffset
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Row {
                val tintColor = MaterialTheme.colorScheme.onPrimary

                Icon(
                    modifier = Modifier
                        .clickable(onClick = onCopy)
                        .weight(1F)
                        .padding(vertical = 25.dp),
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy note",
//                    stringResource(R.string.copy_note),
                    tint = tintColor
                )

                Icon(
                    modifier = Modifier
                        .clickable(onClick = onFavorite)
                        .weight(1F)
                        .padding(vertical = 25.dp),
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "favorite",
//                    stringResource(R.string.favorite),
                    tint = tintColor
                )

                Icon(
                    modifier = Modifier
                        .clickable(onClick = onDelete)
                        .weight(1F)
                        .padding(vertical = 25.dp),
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
//                    stringResource(R.string.delete),
                    tint = tintColor
                )
            }
        }
    }
}

@Preview
@Composable
internal fun NotesSelectionMenuPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        NotesSelectionMenu(visibilityState = true, onDelete = {}, onCopy = {}, onFavorite = {})
    }
}
