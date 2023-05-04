package br.com.leandroferreira.app_sample.screens.addstory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import br.com.leandroferreira.app_sample.theme.BACKGROUND_VARIATION
import br.com.leandroferreira.app_sample.viewmodel.HistoriesViewModel
import br.com.leandroferreira.storyteller.StoryTellerTimeline
import br.com.leandroferreira.storyteller.drawer.DefaultDrawers
import br.com.leandroferreira.storyteller.viewmodel.StoryTellerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull

private const val DEFAULT_DELAY_BEFORE_SCROLL: Long = 200L

@Composable
fun AddStoryScreen() {
    val context = LocalContext.current
    val viewModel: HistoriesViewModel = viewModel(initializer = {
        HistoriesViewModel()
    })
    val storyTellerViewModel: StoryTellerViewModel = viewModel(initializer = {
        StoryTellerViewModel(StoriesRepo(context))
    })
    storyTellerViewModel.requestHistoriesFromApi()
    storyTellerViewModel.updateState()

    ApplicationComposeTheme {
        Scaffold(
            topBar = { TopBar() },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    storyTellerViewModel.updateState()
                    viewModel.toggleEdit()
                }) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "")
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                Body(viewModel, storyTellerViewModel)
            }
        }
    }
}

@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(text = "Edit") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
            TextButton(onClick = { }) {
                Text(
                    text = "Publish",
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 19.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                )
            }
        }
    )
}

@Composable
private fun Body(viewModel: HistoriesViewModel, storyTellerViewModel: StoryTellerViewModel) {
    val storyState by storyTellerViewModel.normalizedStepsStateMap.collectAsStateWithLifecycle()
    val editable by viewModel.editModeState.collectAsStateWithLifecycle()

//    val listState: LazyListState = rememberLazyListState()

    //Todo: Review this. Is a LaunchedEffect the correct way to do this??
//    LaunchedEffect(true, block = {
//        storyTellerViewModel.scrollToPosition.filterNotNull().collect { position ->
//            delay(DEFAULT_DELAY_BEFORE_SCROLL)
//            listState.animateScrollToItem(position, scrollOffset = -200)
//        }
//    })

    Column {
        InfoHeader()

        StoryTellerTimeline(
            modifier = Modifier.fillMaxSize(),
            storyState = storyState,
            contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
            editable = editable,
//            listState = listState,
            drawers = DefaultDrawers.create(
                editable = editable,
                onTextEdit = storyTellerViewModel::onTextEdit,
                onLineBreak = storyTellerViewModel::onLineBreak,
                mergeRequest = storyTellerViewModel::mergeRequest,
                moveRequest = storyTellerViewModel::moveRequest,
                checkRequest = storyTellerViewModel::checkRequest,
                onDeleteRequest = storyTellerViewModel::onDelete
            )
        )
    }
}

@Composable
private fun InfoHeader() {
    Column(modifier = Modifier.background(BACKGROUND_VARIATION)) {
        var storyName by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }

        OutlinedTextField(
            value = storyName,
            onValueChange = { text -> storyName = text },
            label = {
                Text(
                    "Story Name",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Label, contentDescription = "")
            },
        )

        OutlinedTextField(
            value = date,
            onValueChange = { text -> date = text },
            label = {
                Text(
                    "When did it start?",
                    style = TextStyle(fontWeight = FontWeight.Bold),
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Today, contentDescription = "")
            }
        )
    }
}


