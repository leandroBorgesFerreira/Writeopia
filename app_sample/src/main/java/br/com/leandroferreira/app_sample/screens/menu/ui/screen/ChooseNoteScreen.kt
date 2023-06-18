package br.com.leandroferreira.app_sample.screens.menu.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.app_sample.screens.menu.ChooseNoteViewModel
import br.com.leandroferreira.app_sample.screens.menu.ui.dto.DocumentCard
import br.com.leandroferreira.app_sample.utils.ResultData
import com.github.leandroborgesferreira.storyteller.model.document.Document

@Composable
fun ChooseNoteScreen(chooseNoteViewModel: ChooseNoteViewModel, navigateToNote: (String?) -> Unit) {
    chooseNoteViewModel.requestDocuments()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToNote(null) }, content = {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            })
        }
    ) { paddingValues ->
        Content(
            chooseNoteViewModel = chooseNoteViewModel,
            navigateToNote = navigateToNote,
            paddingValues
        )
    }
}

@Composable
fun Content(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String) -> Unit,
    paddingValues: PaddingValues
) {
    when (val documents = chooseNoteViewModel.documentsState.collectAsStateWithLifecycle().value) {
        is ResultData.Complete -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                val data = documents.data

                if (data.isEmpty()) {
                    MockDataScreen(chooseNoteViewModel)
                } else {
                    LazyColumn(content = {
                        items(documents.data) { document ->
                            DocumentItem(document, navigateToNote)
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
fun DocumentItem(documentCard: DocumentCard, documentClick: (String) -> Unit) {
    Box(modifier = Modifier.padding(8.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                documentClick(documentCard.documentId)
            }) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = documentCard.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                documentCard.preview.forEach { storyStep ->
                    Text(storyStep.text ?: "")
                }
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
