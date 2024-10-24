package io.writeopia.note_menu.ui.screen.configuration.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.commonui.options.slide.VerticalOptions
import io.writeopia.note_menu.ui.screen.configuration.modifier.orderConfigModifierVertical
import kotlinx.coroutines.flow.Flow

@Composable
fun ArrangementOptionsMenu(
    selectedState: Flow<Int>,
    staggeredGridSelected: () -> Unit,
    gridSelected: () -> Unit,
    listSelected: () -> Unit,
    width: Dp,
    modifier: Modifier = Modifier,
    selectorShape: Shape = RoundedCornerShape(6.dp)
) {
    Column(modifier = modifier) {
        VerticalOptions(
            width = width,
            selectedState = selectedState,
            options = listOf<Pair<() -> Unit, @Composable ColumnScope.() -> Unit>>(
                staggeredGridSelected to {
                    Icon(
                        modifier = Modifier.orderConfigModifierVertical(onClick = {}),
                        imageVector = Icons.Outlined.Dashboard,
                        contentDescription = "staggered card",
                        //            stringResource(R.string.staggered_card),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                gridSelected to {
                    Icon(
                        modifier = Modifier.orderConfigModifierVertical(onClick = {}),
                        imageVector = Icons.Outlined.GridView,
                        contentDescription = "staggered card",
                        //            stringResource(R.string.staggered_card),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                listSelected to {
                    Icon(
                        modifier = Modifier.orderConfigModifierVertical(onClick = {}),
                        imageVector = Icons.AutoMirrored.Outlined.List,
                        contentDescription = "staggered card",
                        //            stringResource(R.string.staggered_card),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
            ),
            modifier = Modifier.height(110.dp),
            spaceHeight = 2.dp,
            selectorShape = selectorShape
        )
    }
}
