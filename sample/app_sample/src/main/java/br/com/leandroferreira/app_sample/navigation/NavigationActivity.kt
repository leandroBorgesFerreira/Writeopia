package br.com.leandroferreira.app_sample.navigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.leandroferreira.app_sample.di.NotesInjection
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import br.com.leandroferreira.editor.NoteEditorScreen
import br.com.leandroferreira.note_menu.ui.screen.menu.ChooseNoteScreen
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
fun NavigationGraph(
    context: Context = LocalContext.current,
    navController: NavHostController = rememberNavController(),
    database: StoryTellerDatabase = StoryTellerDatabase.database(context),
    sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "br.com.leandroferreira.storyteller.preferences",
        Context.MODE_PRIVATE
    ),
    notesInjection: NotesInjection =  NotesInjection(database, sharedPreferences)
) {

    ApplicationComposeTheme {
        NavHost(navController = navController, startDestination = Destinations.CHOOSE_NOTE.id) {
            composable(Destinations.CHOOSE_NOTE.id) {
                val chooseNoteViewModel = notesInjection.provideChooseNoteViewModel()

                ChooseNoteScreen(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navController::navigateToNote,
                    newNote = navController::navigateToNewNote,
                    navigateUp = navController::navigateUp
                )
            }

            composable(
                route = "${Destinations.NOTE_DETAILS.id}/{noteId}/{noteTitle}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType }),
                enterTransition = {
                    slideInHorizontally(
                        animationSpec = slideInHorizontallyAnimationSpec,
                        initialOffsetX = { intSize -> intSize }
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        animationSpec = slideInHorizontallyAnimationSpec,
                        targetOffsetX = { intSize -> intSize }
                    )
                }
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                val noteTitle = backStackEntry.arguments?.getString("noteTitle")

                if (noteId != null && noteTitle != null) {
                    val noteDetailsViewModel = notesInjection.provideNoteDetailsViewModel()

                    NoteEditorScreen(
                        noteId.takeIf { it != "null" },
                        noteTitle.takeIf { it != "null" },
                        noteDetailsViewModel,
                        navigateBack = navController::navigateToNoteMenu
                    )
                } else {
                    throw IllegalArgumentException("The arguments for this route are wrong!")
                }
            }

            composable(route = Destinations.NOTE_DETAILS.id) {
                NoteEditorScreen(
                    documentId = null,
                    title = null,
                    noteEditorViewModel = notesInjection.provideNoteDetailsViewModel(),
                    navigateBack = navController::navigateToNoteMenu
                )
            }
        }
    }
}

private fun NavController.navigateToNote(id: String, title: String) {
    navigate(
        "${Destinations.NOTE_DETAILS.id}/$id/$title"
    )
}

private fun NavController.navigateToNewNote() {
    navigate(
        Destinations.NOTE_DETAILS.id
    )
}

private fun NavController.navigateToNoteMenu() {
    navigate(
        Destinations.CHOOSE_NOTE.id
    )
}

enum class Destinations(val id: String) {
    NOTE_DETAILS("note_details"),
    CHOOSE_NOTE("choose_note"),
}

