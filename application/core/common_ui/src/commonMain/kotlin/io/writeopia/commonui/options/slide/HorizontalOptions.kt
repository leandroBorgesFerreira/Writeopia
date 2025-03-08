package io.writeopia.commonui.options.slide

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
import androidx.compose.material3.MaterialTheme
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

@Composable
fun HorizontalOptions(
    selectedState: Flow<Int>,
    options: List<Pair<() -> Unit, @Composable RowScope.() -> Unit>>,
    height: Dp = 42.dp,
    spaceWidth: Dp = 9.dp,
    clipShare: Shape = MaterialTheme.shapes.large,
    modifier: Modifier = Modifier,
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
                            .animateItem()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.40F),
                                shape = clipShare
                            )
                    )
                } else {
                    Spacer(
                        modifier = Modifier.fillMaxSize()
                            .height(height)
                            .clip(clipShare)
                            .clickable {
                                options[i].first.invoke()
                            }
                            .animateItem()
                    )
                }
            }
        }
    }
}
