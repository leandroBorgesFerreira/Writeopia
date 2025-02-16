package io.writeopia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.span.Span
import io.writeopia.ui.icons.WrSdkIcons
import org.jetbrains.compose.ui.tooling.preview.Preview

// Here
// This screen could live in a module for extra Composables. In the future there will be more
// buttons here
@Composable
fun EditionScreen(
    modifier: Modifier = Modifier,
    onSpanClick: (Span) -> Unit = {},
    checkboxClick: () -> Unit = {},
    listItemClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier
                .clickable {
                    onSpanClick(Span.BOLD)
                }
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = Icons.Outlined.FormatBold,
            contentDescription = "BOLD",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Icon(
            modifier = Modifier
                .clickable {
                    onSpanClick(Span.ITALIC)
                }
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = Icons.Outlined.FormatItalic,
            contentDescription = "ITALIC",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Icon(
            modifier = Modifier
                .clickable {
                    onSpanClick(Span.UNDERLINE)
                }
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = Icons.Outlined.FormatUnderlined,
            contentDescription = "UNDERLINE",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Icon(
            modifier = Modifier
                .clickable(onClick = checkboxClick)
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = WrSdkIcons.checkbox,
            contentDescription = "Checkbox",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Icon(
            modifier = Modifier
                .clickable(onClick = listItemClick)
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = WrSdkIcons.list,
            contentDescription = "List item",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Icon(
            modifier = Modifier
                .clickable(onClick = onDelete)
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "Delete",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.weight(1F))

        Icon(
            modifier = Modifier
                .clickable(onClick = onClose)
                .size(50.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageVector = WrSdkIcons.close,
            contentDescription = "List item",
//            contentDescription = stringResource(R.string.delete),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun EditionScreenPreview() {
    EditionScreen()
}
