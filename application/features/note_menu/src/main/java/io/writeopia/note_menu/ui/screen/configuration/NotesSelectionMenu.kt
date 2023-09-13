package io.writeopia.note_menu.ui.screen.configuration

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.writeopia.appresourcers.R
import io.writeopia.common_ui.SlideInBox

@Composable
internal fun BoxScope.NotesSelectionMenu(
    modifier: Modifier = Modifier,
    visibilityState: Boolean,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onFavorite: () -> Unit,
) {

    val animationSpec = spring(
        stiffness = 4000F,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )

    SlideInBox(
        modifier = modifier.align(Alignment.BottomCenter),
        editState = visibilityState,
        showBackground = false,
        outsideClick = { },
        enterAnimationSpec = animationSpec,
        exitAnimationSpec = animationSpec,
        animationLabel = "configurationsMenuAnimation"
    ) {
        val topCorner = CornerSize(16.dp)
        val bottomCorner = CornerSize(0.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topCorner,
                        topCorner,
                        bottomCorner,
                        bottomCorner
                    )
                )
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
                    contentDescription = stringResource(
                        R.string.copy_note
                    ),
                    tint = tintColor
                )

                Icon(
                    modifier = Modifier
                        .clickable(onClick = onFavorite)
                        .weight(1F)
                        .padding(vertical = 25.dp),
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(
                        R.string.favorite
                    ),
                    tint = tintColor
                )

                Icon(
                    modifier = Modifier
                        .clickable(onClick = onDelete)
                        .weight(1F)
                        .padding(vertical = 25.dp),
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = stringResource(
                        R.string.delete
                    ),
                    tint = tintColor
                )
            }
        }
    }
}

@Composable
@Preview
internal fun NotesSelectionMenuPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        NotesSelectionMenu(visibilityState = true, onDelete = {}, onCopy = {}, onFavorite = {})
    }
}
