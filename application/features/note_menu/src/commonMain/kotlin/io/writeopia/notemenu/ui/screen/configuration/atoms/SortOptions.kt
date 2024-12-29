package io.writeopia.notemenu.ui.screen.configuration.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.notemenu.ui.screen.configuration.modifier.icon
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun SortOptions(
    showSortingOption: StateFlow<Boolean>,
    showSortOptionsRequest: () -> Unit,
    hideSortOptionsRequest: () -> Unit,
    selectSortOption: (OrderBy) -> Unit,
    sortOptionState: StateFlow<OrderBy>,
    modifier: Modifier = Modifier,
) {
    val showSorting by showSortingOption.collectAsState()

    Box(modifier = modifier) {
        Icon(
            imageVector = WrIcons.sort,
            contentDescription = "Sort Options",
            modifier = Modifier.icon(showSortOptionsRequest).align(Alignment.Center).padding(2.dp),
            tint = MaterialTheme.colorScheme.onBackground,
        )

        DropdownMenu(
            expanded = showSorting,
            onDismissRequest = hideSortOptionsRequest,
            offset = DpOffset((-30).dp, (-6).dp)
        ) {
            val sortOption by sortOptionState.collectAsState()
            val iconTintColor = MaterialTheme.colorScheme.onPrimary

            val background = @Composable { orderBy: OrderBy ->
                if (orderBy == sortOption) {
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25F)
                } else {
                    MaterialTheme.colorScheme.primary
                }
            }

            DropdownMenuItem(
                modifier = Modifier.background(background(OrderBy.NAME)),
                leadingIcon = {
                    Icon(
                        imageVector = WrIcons.sortByName,
                        contentDescription = "Sort by name",
                        tint = iconTintColor
                    )
                },
                onClick = {
                    selectSortOption(OrderBy.NAME)
                },
                text = {
                    Text("Sort by name", color = MaterialTheme.colorScheme.onPrimary)
                }
            )

            DropdownMenuItem(
                modifier = Modifier.background(background(OrderBy.CREATE)),
                leadingIcon = {
                    Icon(
                        imageVector = WrIcons.sortByCreated,
                        contentDescription = "Sort by creation",
                        tint = iconTintColor
                    )
                },
                onClick = {
                    selectSortOption(OrderBy.CREATE)
                },
                text = {
                    Text("Sort by creation", color = MaterialTheme.colorScheme.onPrimary)
                }
            )

            DropdownMenuItem(
                modifier = Modifier.background(background(OrderBy.UPDATE)),
                leadingIcon = {
                    Icon(
                        imageVector = WrIcons.sortByUpdate,
                        contentDescription = "Sort by last update",
                        tint = iconTintColor
                    )
                },
                onClick = {
                    selectSortOption(OrderBy.UPDATE)
                },
                text = {
                    Text(
                        "Sort by last update",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        }
    }
}
