package io.writeopia.common_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalOptions(
    selectedState: Flow<Int>,
    options: List<Pair<() -> Unit, @Composable RowScope.() -> Unit>>,
    height: Dp = 42.dp,
    modifier: Modifier = Modifier,
    spaceWidth: Dp = 9.dp,
) {
    Box(modifier = modifier.height(height)) {
        Row {
            options.forEachIndexed { i, (_, composable) ->
                composable()

                if (i != options.lastIndex) {
                    Spacer(modifier = Modifier.width(spaceWidth))
                }
            }
        }

        val selected by selectedState.collectAsState(0)
        val shape = RoundedCornerShape(6.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(options.size),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(spaceWidth)
        ) {
            items(
                options.size,
                key = { index ->
                    if (index == selected) "ha" else Random.nextInt().toString()
                }
            ) { i ->
                if (i == selected) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(height)
//                            .padding(start = 3.dp, end = 3.dp, top = 3.dp, bottom = 3.dp)
                            .animateItemPlacement()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25F),
                                shape = shape
                            )
                    )
                } else {
                    Spacer(
                        modifier = Modifier.fillMaxSize()
                            .height(height)
                            .clip(shape)
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
