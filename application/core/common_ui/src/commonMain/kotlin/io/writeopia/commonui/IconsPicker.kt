package io.writeopia.commonui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.icons.WrIcons

@Composable
fun IconsPicker(
    modifier: Modifier = Modifier.size(200.dp),
    icons: Map<String, ImageVector> = WrIcons.allIcons,
    tintColor: Color = LocalContentColor.current,
    iconSize: Dp = 24.dp,
    iconSelect: (String) -> Unit,
) {
    val spacing = Arrangement.spacedBy(8.dp)

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(6),
        horizontalArrangement = spacing,
        verticalArrangement = spacing,
        contentPadding = PaddingValues(10.dp)
    ) {
        items(icons.toList()) { (name, vector) ->
            Icon(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { iconSelect(name) }
                    .padding(4.dp)
                    .size(iconSize),
                imageVector = vector,
                contentDescription = name,
                tint = tintColor
            )
        }
    }
}
