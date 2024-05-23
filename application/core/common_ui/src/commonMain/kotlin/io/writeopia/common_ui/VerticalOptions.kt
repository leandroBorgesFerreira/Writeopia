package io.writeopia.common_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalOptions(
    selectedState: Flow<Int>,
    options: List<Pair<() -> Unit, @Composable ColumnScope.() -> Unit>>,
    width: Dp = 42.dp,
    modifier: Modifier = Modifier,
    spaceHeight: Dp = 9.dp,
    selectorShape: Shape = RoundedCornerShape(6.dp)
) {
    Box(modifier = modifier.width(width)) {
        Column {
            options.forEachIndexed { i, (_, composable) ->
                composable()

                if (i != options.lastIndex) {
                    Spacer(modifier = Modifier.height(spaceHeight))
                }
            }
        }

        val selected by selectedState.collectAsState(0)

        LazyHorizontalGrid(
            rows = GridCells.Fixed(options.size),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spaceHeight)
        ) {
            items(
                options.size,
                key = { index ->
                    if (index == selected) "ha" else index
                }
            ) { i ->
                if (i == selected) {
                    Box(
                        modifier = Modifier
                            .width(width)
                            .animateItemPlacement()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25F),
                                shape = selectorShape
                            )
                    )
                } else {
                    Spacer(
                        modifier = Modifier
                            .width(width)
                            .clip(selectorShape)
                            .clickable {
                                options[i].first.invoke()
                            }
                            .animateItemPlacement()
                    )
                }
            }
        }
    }
}
