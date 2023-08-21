package com.github.leandroborgesferreira.storytellerapp.navigation

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.leandroborgesferreira.storyteller.network.injector.ApiInjector
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.video.VideoFrameConfig
import com.github.leandroborgesferreira.storytellerapp.auth.di.AuthInjection
import com.github.leandroborgesferreira.storytellerapp.auth.navigation.authNavigation
import com.github.leandroborgesferreira.storytellerapp.auth.navigation.navigateToAuthMenu
import com.github.leandroborgesferreira.storytellerapp.di.NotesInjection
import com.github.leandroborgesferreira.storytellerapp.editor.NoteEditorScreen
import com.github.leandroborgesferreira.storytellerapp.note_menu.ui.screen.menu.ChooseNoteScreen
import com.github.leandroborgesferreira.storytellerapp.theme.ApplicationComposeTheme
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VideoFrameConfig.configCoilForVideoFrame(this)

        setContent {
            NavigationGraph(application = application)
        }
    }
}

@Composable
fun NavigationGraph(
    application: Application,
    navController: NavHostController = rememberNavController(),
    database: StoryTellerDatabase = StoryTellerDatabase.database(application),
    sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "com.github.leandroborgesferreira.storytellerapp.preferences",
        Context.MODE_PRIVATE
    ),
) {

    val notesInjection = NotesInjection(database, sharedPreferences, application)
    val authInjection = AuthInjection(database, apiInjector = ApiInjector())

    val startDestination = Destinations.CHOOSE_NOTE.id

    ApplicationComposeTheme {
        NavHost(navController = navController, startDestination = startDestination) {
            authNavigation(navController, authInjection, navController::navigateToMainMenu)

            composable(Destinations.CHOOSE_NOTE.id) {
                val chooseNoteViewModel = notesInjection.provideChooseNoteViewModel()

                ChooseNoteScreen(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navController::navigateToNote,
                    newNote = navController::navigateToNewNote,
                    onLogout = {
                        navController.navigateToAuthMenu()
                    }
                )
            }

            composable(
                route = "${Destinations.NOTE_DETAILS.id}/{noteId}/{noteTitle}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType }),
//                enterTransition = {
//                    slideInHorizontally(
//                        animationSpec = slideInHorizontallyAnimationSpec,
//                        initialOffsetX = { intSize -> intSize }
//                    )
//                },
//                exitTransition = {
//                    slideOutHorizontally(
//                        animationSpec = slideInHorizontallyAnimationSpec,
//                        targetOffsetX = { intSize -> intSize }
//                    )
//                }
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
