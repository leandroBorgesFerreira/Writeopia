package io.writeopia.note_menu.ui.screen.configuration.molecules

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.commonui.SlideInBox
import io.writeopia.commonui.options.slide.HorizontalOptions
import io.writeopia.note_menu.ui.screen.configuration.modifier.orderConfigModifierHorizontal
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val INNER_PADDING = 3

@Composable
internal fun BoxScope.MobileConfigurationsMenu(
    selected: Flow<Int>,
    visibilityState: Boolean,
    outsideClick: () -> Unit,
    staggeredGridOptionClick: () -> Unit,
    gridOptionClick: () -> Unit,
    listOptionClick: () -> Unit,
    sortingSelected: (OrderBy) -> Unit,
    modifier: Modifier = Modifier,
) {
    SlideInBox(
        modifier = modifier.align(Alignment.BottomCenter),
        editState = visibilityState,
        outsideClick = outsideClick,
        enterAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntOffset.VisibilityThreshold
        ),
        animationLabel = "configurationsMenuAnimation"
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clip(
                    MaterialTheme.shapes.large
                )
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ArrangementSection(selected, staggeredGridOptionClick, gridOptionClick, listOptionClick)

            SortingSection(sortingSelected = sortingSelected)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SectionText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 6.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 18.sp
        ),
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ArrangementOptions(
    selected: Flow<Int>,
    staggeredGridOptionClick: () -> Unit,
    gridOptionClick: () -> Unit,
    listOptionClick: () -> Unit,
    height: Dp = 42.dp,
) {
    HorizontalOptions(
        selectedState = selected,
        options = listOf<Pair<() -> Unit, @Composable RowScope.() -> Unit>>(
            staggeredGridOptionClick to {
                Icon(
                    modifier = Modifier
                        .orderConfigModifierHorizontal(clickable = gridOptionClick)
                        .weight(1F),
                    imageVector = Icons.Outlined.Dashboard,
                    contentDescription = "staggered card",
                    //            stringResource(R.string.staggered_card),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            gridOptionClick to {
                Icon(
                    modifier = Modifier
                        .orderConfigModifierHorizontal(clickable = gridOptionClick)
                        .weight(1F),
                    imageVector = Icons.Outlined.GridView,
                    contentDescription = "staggered card",
                    //            stringResource(R.string.staggered_card),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            listOptionClick to {
                Icon(
                    modifier = Modifier
                        .orderConfigModifierHorizontal(clickable = listOptionClick)
                        .weight(1F),
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = "note list",
                    //            stringResource(R.string.note_list),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        ),
        modifier = Modifier.fillMaxWidth().padding(INNER_PADDING.dp),
        height = height
    )
}

@Composable
private fun ArrangementSection(
    selected: Flow<Int>,
    staggeredGridOptionClick: () -> Unit,
    gridOptionClick: () -> Unit,
    listOptionClick: () -> Unit
) {
    SectionText(
        text = "Arrangement"
//        stringResource(
//            R.string.arrangement
//        )
    )

    ArrangementOptions(
        selected = selected,
        staggeredGridOptionClick = staggeredGridOptionClick,
        gridOptionClick = gridOptionClick,
        listOptionClick = listOptionClick,
    )
}

@Composable
private fun SortingSection(sortingSelected: (OrderBy) -> Unit) {
    SectionText(
        text = "Sorting"
//    stringResource(R.string.sorting)
    )
    val optionStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = INNER_PADDING.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.inverseSurface)
    ) {
        Text(
            modifier = Modifier
                .clickable { sortingSelected(OrderBy.UPDATE) }
                .sortingOptionModifier(),
            text = "Last updated",
//            stringResource(R.string.last_updated)
            style = optionStyle,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

        Text(
            modifier = Modifier
                .clickable { sortingSelected(OrderBy.CREATE) }
                .sortingOptionModifier(),
            text = "Create",
//            stringResource(R.string.last_created),
            style = optionStyle,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

        Text(
            modifier = Modifier
                .clickable { sortingSelected(OrderBy.NAME) }
                .sortingOptionModifier(),
            text = "Name",
//            stringResource(R.string.name),
            style = optionStyle,
        )
    }
}

private fun Modifier.sortingOptionModifier(): Modifier = fillMaxWidth().padding(12.dp)

@Preview
@Composable
private fun ConfigurationsMenu_Preview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        MobileConfigurationsMenu(MutableStateFlow(1), true, {}, {}, {}, {}, {}, Modifier)
    }
}

@Preview
@Composable
private fun ArrangementOptions_Preview() {
    ArrangementOptions(MutableStateFlow(1), {}, {}, {})
}
