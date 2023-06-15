package br.com.leandroferreira.app_sample.screens.addstory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.leandroborgesferreira.storyteller.StoryTellerEditor
import com.github.leandroborgesferreira.storyteller.drawer.DefaultDrawers


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStoryScreen(storiesViewModel: StoriesViewModel) {

    storiesViewModel.requestStories()

    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                storiesViewModel.toggleEdit()
            }) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            Body(storiesViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    )
}

@Composable
private fun Body(storiesViewModel: StoriesViewModel) {
    val storyState by storiesViewModel.story.collectAsStateWithLifecycle()
    val editable by storiesViewModel.editModeState.collectAsStateWithLifecycle()

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

        StoryTellerEditor(
            modifier = Modifier.fillMaxSize(),
            storyState = storyState,
            contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
            editable = editable,
//            listState = listState,
            drawers = DefaultDrawers.create(editable, storiesViewModel.storyTellerManager)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoHeader() {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
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


