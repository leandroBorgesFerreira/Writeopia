package br.com.leandroferreira.app_sample.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.leandroferreira.app_sample.screens.menu.ChooseNoteScreen
import br.com.leandroferreira.app_sample.screens.menu.ChooseNoteViewModel
import br.com.leandroferreira.app_sample.screens.menu.NotesRepository
import br.com.leandroferreira.app_sample.screens.note.NoteDetailsScreen
import br.com.leandroferreira.app_sample.screens.note.NoteDetailsViewModel
import br.com.leandroferreira.app_sample.screens.note.NoteDetailsViewModelFactory
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import com.github.leandroborgesferreira.storyteller.VideoFrameConfig
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepository

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VideoFrameConfig.configCoilForVideoFrame(this)

        setContent {
            NavigationGraph()
        }
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = StoryTellerDatabase.database(context)

    ApplicationComposeTheme {

        NavHost(navController = navController, startDestination = Destinations.CHOOSE_NOTE.id) {
            composable(Destinations.CHOOSE_NOTE.id) {
                val notesRepository = NotesRepository(
                    database.documentDao(),
                    database.storyUnitDao()
                )
                val chooseNoteViewModel = ChooseNoteViewModel(notesRepository)

                ChooseNoteScreen(chooseNoteViewModel = chooseNoteViewModel) { noteId ->
                    navController.navigate("${Destinations.NOTE_DETAILS.id}/$noteId")
                }
            }

            composable(
                route = "${Destinations.NOTE_DETAILS.id}/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("noteId")?.let { id ->
                    val repository = DocumentRepository(
                        database.documentDao(),
                        database.storyUnitDao()
                    )
                    val storyTellerManager = StoryTellerManager()

                    val noteDetailsViewModel: NoteDetailsViewModel =
                        viewModel(
                            factory = NoteDetailsViewModelFactory(
                                storyTellerManager,
                                repository
                            )
                        )

                    NoteDetailsScreen(id.takeIf { it != "null" }, noteDetailsViewModel)
                }
            }
        }
    }
}

enum class Destinations(val id: String) {
    NOTE_DETAILS("note_details"),
    CHOOSE_NOTE("choose_note"),
}

