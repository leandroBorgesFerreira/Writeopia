package io.writeopia.editor.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FontDownload
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SideEditorOptions(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.shapes.medium
        ).background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)

    ) {
        val spacing = 3.dp

        Spacer(modifier = Modifier.height(spacing))

        Icon(
            imageVector = Icons.Outlined.Style,
            contentDescription = "Document Style",
            modifier = Modifier
                .padding(horizontal = spacing)
                .clip(MaterialTheme.shapes.medium)
                .clickable { }
                .size(40.dp)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(1.dp))

        Icon(
            imageVector = Icons.Outlined.FontDownload,
            contentDescription = "Document Style",
            modifier = Modifier
                .padding(horizontal = spacing)
                .clip(MaterialTheme.shapes.medium)
                .clickable { }
                .size(40.dp)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(spacing))
    }
}
