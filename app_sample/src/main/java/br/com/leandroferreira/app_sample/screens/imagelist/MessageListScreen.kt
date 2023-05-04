package br.com.leandroferreira.app_sample.screens.imagelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import br.com.leandroferreira.storyteller.StoryTellerTimeline
import br.com.leandroferreira.storyteller.drawer.DefaultDrawers
import br.com.leandroferreira.storyteller.viewmodel.StoryTellerViewModel

@Composable
fun ImageListScreen() {
    val storyTellerViewModel: StoryTellerViewModel = viewModel(initializer = {
        StoryTellerViewModel(ImageListRepo())
    })
    storyTellerViewModel.requestHistoriesFromApi()
    storyTellerViewModel.updateState()

    ApplicationComposeTheme {
        Scaffold(
            topBar = { TopBar() },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                Body(storyTellerViewModel)
            }
        }
    }
}

@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(text = "Images") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
    )
}

@Composable
private fun Body(storyTellerViewModel: StoryTellerViewModel) {
    val storyState by storyTellerViewModel.normalizedStepsStateMap.collectAsStateWithLifecycle()

    Column {
        StoryTellerTimeline(
            modifier = Modifier.fillMaxSize(),
            storyState = storyState,
            contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
            editable = false,
            drawers = DefaultDrawers.create(
                editable = false,
                onTextEdit = storyTellerViewModel::onTextEdit,
                onLineBreak =storyTellerViewModel::onLineBreak,
                mergeRequest = storyTellerViewModel::mergeRequest,
                moveRequest = storyTellerViewModel::moveRequest,
                onDeleteRequest = storyTellerViewModel::onDelete
            )
        )
    }
}
