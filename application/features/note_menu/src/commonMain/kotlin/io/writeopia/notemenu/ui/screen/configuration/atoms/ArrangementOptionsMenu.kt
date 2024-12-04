package io.writeopia.notemenu.ui.screen.configuration.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.commonui.options.slide.VerticalOptions
import io.writeopia.notemenu.ui.screen.configuration.modifier.orderConfigModifierVertical
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
                        modifier = Modifier.orderConfigModifierVertical(onClick = {})
                            .padding(vertical = 6.dp),
                        imageVector = WrIcons.layoutStaggeredGrid,
                        contentDescription = "staggered card",
                        //            stringResource(R.string.staggered_card),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                gridSelected to {
                    Icon(
                        modifier = Modifier.orderConfigModifierVertical(onClick = {})
                            .padding(vertical = 6.dp),
                        imageVector = WrIcons.layoutGrid,
                        contentDescription = "staggered card",
                        //            stringResource(R.string.staggered_card),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                listSelected to {
                    Icon(
                        modifier = Modifier.orderConfigModifierVertical(onClick = {})
                            .padding(vertical = 6.dp),
                        imageVector = WrIcons.layoutList,
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
