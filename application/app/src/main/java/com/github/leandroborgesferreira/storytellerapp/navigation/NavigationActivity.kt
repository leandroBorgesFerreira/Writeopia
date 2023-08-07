package com.github.leandroborgesferreira.storytellerapp.navigation

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
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.video.VideoFrameConfig
import com.github.leandroborgesferreira.storytellerapp.auth.di.AuthInjections
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginScreenBinding
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuScreen
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterScreenBinding
import com.github.leandroborgesferreira.storytellerapp.di.NotesInjection
import com.github.leandroborgesferreira.storytellerapp.editor.NoteEditorScreen
import com.github.leandroborgesferreira.storytellerapp.note_menu.ui.screen.menu.ChooseNoteScreen
import com.github.leandroborgesferreira.storytellerapp.theme.ApplicationComposeTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VideoFrameConfig.configCoilForVideoFrame(this)

        setContent {
            NavigationGraph(activity = this)
        }
    }
}

@Composable
fun NavigationGraph(
    context: Context = LocalContext.current,
    activity: NavigationActivity,
    navController: NavHostController = rememberNavController(),
    database: StoryTellerDatabase = StoryTellerDatabase.database(context),
    sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "com.github.leandroborgesferreira.storytellerapp.preferences",
        Context.MODE_PRIVATE
    ),
    notesInjection: NotesInjection = NotesInjection(database, sharedPreferences),
    authInjections: AuthInjections = AuthInjections(activity)
) {

    val currentUser = Firebase.auth.currentUser

    val startDestination = if (currentUser != null) {
        Destinations.CHOOSE_NOTE.id
    } else {
        Destinations.AUTH_MENU.id
    }

    ApplicationComposeTheme {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Destinations.AUTH_MENU.id) {
                AuthMenuScreen(
                    navigateToLogin = navController::navigateAuthLogin,
                    navigateToRegister = navController::navigateAuthRegister,
                )
            }

            composable(Destinations.AUTH_REGISTER.id) {
                val registerViewModel = authInjections.provideRegisterViewModel()
                RegisterScreenBinding(registerViewModel, navController::navigateToMainMenu)
            }

            composable(Destinations.AUTH_LOGIN.id) {
                val loginViewModel = authInjections.provideLoginViewModel()
                LoginScreenBinding(loginViewModel, navController::navigateToMainMenu)
            }

            composable(Destinations.CHOOSE_NOTE.id) {
                val chooseNoteViewModel = notesInjection.provideChooseNoteViewModel()

                ChooseNoteScreen(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navController::navigateToNote,
                    newNote = navController::navigateToNewNote,
                    logout = {
                        Firebase.auth.signOut()
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

enum class Destinations(val id: String) {
    NOTE_DETAILS("note_details"),
    CHOOSE_NOTE("choose_note"),
    AUTH_REGISTER("auth_register"),
    AUTH_MENU("auth_menu"),
    AUTH_LOGIN("auth_login"),
}

