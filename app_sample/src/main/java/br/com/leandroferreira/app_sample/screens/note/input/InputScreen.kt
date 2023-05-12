package br.com.leandroferreira.app_sample.screens.note.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.app_sample.R

@Composable
fun InputScreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.align(Alignment.Center)) {
                Icon(
                    imageVector = Icons.Default.Undo,
                    contentDescription = stringResource(R.string.undo)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Icon(
                    imageVector = Icons.Default.Redo,
                    contentDescription = stringResource(R.string.redo)
                )
            }
        }
    }
}

@Preview
@Composable
fun InputComposablePreview() {
    InputScreen()
}
