package com.github.leandroborgesferreira.storytellerapp.navigation

import android.app.Application
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
import com.github.leandroborgesferreira.storytellerapp.di.NotesInjection
import com.github.leandroborgesferreira.storytellerapp.theme.ApplicationComposeTheme
import com.github.leandroborgesferreira.storytellerapp.editor.NoteEditorScreen
import com.github.leandroborgesferreira.storytellerapp.note_menu.ui.screen.menu.ChooseNoteScreen
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.video.VideoFrameConfig
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginScreenBinding
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuScreen
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterScreenBinding
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel

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
    context: Context = LocalContext.current,
    navController: NavHostController = rememberNavController(),
    database: StoryTellerDatabase = StoryTellerDatabase.database(context),
    sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "com.github.leandroborgesferreira.storytellerapp.preferences",
        Context.MODE_PRIVATE
    ),
    notesInjection: NotesInjection = NotesInjection(database, sharedPreferences),
    application: Application
) {

    ApplicationComposeTheme {
        NavHost(navController = navController, startDestination = Destinations.AUTH_MENU.id) {
            composable(Destinations.AUTH_MENU.id) {
                AuthMenuScreen(
                    navigateToLogin = navController::navigateAuthLogin,
                    navigateToRegister = navController::navigateAuthRegister,
                    navigateToApp = navController::navigateToMainMenu,
                )
            }

            composable(Destinations.AUTH_REGISTER.id) {
                val registerViewModel = RegisterViewModel(application)
                RegisterScreenBinding(registerViewModel)
            }

            composable(Destinations.AUTH_LOGIN.id) {
                val loginViewModel = LoginViewModel(application)
                LoginScreenBinding(loginViewModel)
            }

            composable(Destinations.CHOOSE_NOTE.id) {
                val chooseNoteViewModel = notesInjection.provideChooseNoteViewModel()

                ChooseNoteScreen(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navController::navigateToNote,
                    newNote = navController::navigateToNewNote,
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

private fun NavController.navigateToNote(id: String, title: String) {
    navigate(
        "${Destinations.NOTE_DETAILS.id}/$id/$title"
    )
}

private fun NavController.navigateToMainMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}

private fun NavController.navigateToNewNote() {
    navigate(
        Destinations.NOTE_DETAILS.id
    )
}

private fun NavController.navigateAuthRegister() {
    navigate(
        Destinations.AUTH_REGISTER.id
    )
}

private fun NavController.navigateAuthLogin() {
    navigate(
        Destinations.AUTH_LOGIN.id
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
    AUTH_REGISTER("auth_register"),
    AUTH_MENU("auth_menu"),
    AUTH_LOGIN("auth_login"),
}

