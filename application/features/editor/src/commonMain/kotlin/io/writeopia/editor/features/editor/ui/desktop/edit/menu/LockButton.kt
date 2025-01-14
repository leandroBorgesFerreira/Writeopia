package io.writeopia.editor.features.editor.ui.desktop.edit.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.theme.WriteopiaTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LockButton(
    isEditableState: StateFlow<Boolean>,
    setEditable: () -> Unit,
    defaultColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    selectedColor: Color = WriteopiaTheme.colorScheme.highlight
) {
    val isEditable by isEditableState.collectAsState()
    val lockButtonColor = if (isEditable) defaultColor else selectedColor

    val shape = MaterialTheme.shapes.medium

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(lockButtonColor, shape)
            .clip(shape)
            .clickable { setEditable() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = WrIcons.lock,
            contentDescription = "Lock",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            "Lock",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
