package br.com.leandroferreira.app_sample.screens.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.app_sample.utils.ResultData

@Composable
fun ChooseNoteScreen(chooseNoteViewModel: ChooseNoteViewModel, navigateToNote: (String) -> Unit) {
    chooseNoteViewModel.requestDocuments()

    Content(chooseNoteViewModel = chooseNoteViewModel, navigateToNote = navigateToNote)
}

@Composable
fun Content(chooseNoteViewModel: ChooseNoteViewModel, navigateToNote: (String) -> Unit) {
    when (val documents = chooseNoteViewModel.documentsState.collectAsStateWithLifecycle().value) {
        is ResultData.Complete -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                val data = documents.data

                if (data.isEmpty()) {
                    MockDataScreen(chooseNoteViewModel)
                } else {
                    LazyColumn(content = {
                        items(documents.data) { document ->
                            Text(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        navigateToNote(document.id)
                                    },
                                text = document.title
                            )
                        }
                    })
                }
            }
        }

        is ResultData.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.error_loading_notes)
                )
            }
        }

        is ResultData.Loading, is ResultData.Idle -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun MockDataScreen(chooseNoteViewModel: ChooseNoteViewModel) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.you_dont_have_notes)
            )
            Button(onClick = {
                chooseNoteViewModel.addMockData(context)
            }) {
                Text(text = stringResource(R.string.add_sample_notes))
            }
        }
    }
}
