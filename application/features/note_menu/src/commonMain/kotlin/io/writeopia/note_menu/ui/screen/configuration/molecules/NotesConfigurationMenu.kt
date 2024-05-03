package io.writeopia.note_menu.ui.screen.configuration.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.writeopia.note_menu.ui.screen.configuration.atoms.ArrangementOptionsMenu
import io.writeopia.note_menu.ui.screen.configuration.atoms.SortOptions
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NotesConfigurationMenu(
    showSortingOption: StateFlow<Boolean>,
    selectedState: Flow<Int>,
    showSortOptionsRequest: () -> Unit,
    hideSortOptionsRequest: () -> Unit,
    selectSortOption: (OrderBy) -> Unit,
    staggeredGridSelected: () -> Unit,
    gridSelected: () -> Unit,
    listSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArrangementOptionsMenu(
            selectedState = selectedState,
            staggeredGridSelected = staggeredGridSelected,
            gridSelected = gridSelected,
            listSelected = listSelected
        )

        HorizontalDivider(color = Color.LightGray)

        SortOptions(
            showSortingOption,
            showSortOptionsRequest,
            hideSortOptionsRequest,
            selectSortOption
        )
    }
}
