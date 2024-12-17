package io.writeopia.features.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.writeopia.features.search.repository.SearchItem
import io.writeopia.features.search.ui.SearchScreen
import io.writeopia.notemenu.data.model.NotesNavigation
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DocumentsSearchScreen(
    searchState: StateFlow<String>,
    searchResults: StateFlow<List<SearchItem>>,
    onSearchType: (String) -> Unit,
    documentClick: (String, String) -> Unit,
    onFolderClick: (NotesNavigation) -> Unit,
) {
    SearchScreen(
        modifier = Modifier.fillMaxSize(),
        searchState,
        searchResults,
        onSearchType,
        documentClick,
        onFolderClick
    )
}
