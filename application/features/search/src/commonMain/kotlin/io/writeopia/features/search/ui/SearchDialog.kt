package io.writeopia.features.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.writeopia.common.utils.icons.WrIcons
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SearchDialog(
    searchState: StateFlow<String>,
    onSearchType: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(modifier = modifier.width(750.dp).height(500.dp), shape = MaterialTheme.shapes.large) {
            Column {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = WrIcons.search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    val search by searchState.collectAsState()

                    Box {
                        BasicTextField(
                            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                            value = search,
                            onValueChange = onSearchType,
                            singleLine = true,
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
                        )

                        if (search.isEmpty()) {
                            Text("Search", )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
