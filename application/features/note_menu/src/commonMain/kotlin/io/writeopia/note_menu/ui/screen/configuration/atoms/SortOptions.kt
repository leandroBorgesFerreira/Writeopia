package io.writeopia.note_menu.ui.screen.configuration.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.configuration.modifier.icon
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun SortOptions(
    showSortingOption: StateFlow<Boolean>,
    showSortOptionsRequest: () -> Unit,
    hideSortOptionsRequest: () -> Unit,
    selectSortOption: (OrderBy) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showSorting by showSortingOption.collectAsState()

    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.SwapVert,
            contentDescription = "Sort Options",
            modifier = Modifier.icon(showSortOptionsRequest).align(Alignment.Center),
            tint = MaterialTheme.colorScheme.onBackground,
        )

        DropdownMenu(
            expanded = showSorting,
            onDismissRequest = hideSortOptionsRequest,
            offset = DpOffset((-30).dp, (-6).dp)
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = "Sort by name",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }, onClick = {
                    selectSortOption(OrderBy.NAME)
                },
                text = {
                    Text("Sort by name", color = MaterialTheme.colorScheme.onPrimary)
                }
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = "Sort by creation",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }, onClick = {
                    selectSortOption(OrderBy.CREATE)
                },
                text = {
                    Text("Sort by creation", color = MaterialTheme.colorScheme.onPrimary)
                }
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = "Sort by last update",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }, onClick = {
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
