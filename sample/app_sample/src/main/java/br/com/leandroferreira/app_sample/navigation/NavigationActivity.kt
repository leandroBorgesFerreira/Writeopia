package br.com.leandroferreira.app_sample.navigation

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.leandroferreira.app_sample.di.NotesInjection
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import br.com.leandroferreira.editor.NoteDetailsScreen
import br.com.leandroferreira.note_menu.ui.screen.ChooseNoteScreen
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.video.VideoFrameConfig

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
    val sharedPreferences = context.getSharedPreferences(
        "br.com.leandroferreira.storyteller.preferences",
        Context.MODE_PRIVATE
    )

    val notesInjection = NotesInjection(database, sharedPreferences)

    ApplicationComposeTheme {
        NavHost(navController = navController, startDestination = Destinations.CHOOSE_NOTE.id) {
            composable(Destinations.CHOOSE_NOTE.id) {
                val chooseNoteViewModel = notesInjection.provideChooseNoteViewModel()

                ChooseNoteScreen(chooseNoteViewModel = chooseNoteViewModel) { noteId, noteTitle ->
                    navController.navigate(
                        "${Destinations.NOTE_DETAILS.id}/$noteId/$noteTitle"
                    )
                }
            }

            composable(
                route = "${Destinations.NOTE_DETAILS.id}/{noteId}/{noteTitle}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                val noteTitle = backStackEntry.arguments?.getString("noteTitle")

                if (noteId != null && noteTitle != null) {
                    val noteDetailsViewModel = notesInjection.provideNoteDetailsViewModel()

                    NoteDetailsScreen(
                        noteId.takeIf { it != "null" },
                        noteTitle.takeIf { it != "null" },
                        noteDetailsViewModel,
                        navigateBack = {
                            navController.navigateUp()
                        }
                    )
                } else {
                    throw IllegalArgumentException("Wrong route!")
                }
            }
        }
    }
}

enum class Destinations(val id: String) {
    NOTE_DETAILS("note_details"),
    CHOOSE_NOTE("choose_note"),
}

