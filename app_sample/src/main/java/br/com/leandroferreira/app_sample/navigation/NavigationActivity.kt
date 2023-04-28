package br.com.leandroferreira.app_sample.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.leandroferreira.app_sample.screens.addstory.AddStoryScreen
import br.com.leandroferreira.app_sample.screens.imagelist.ImageListScreen
import br.com.leandroferreira.app_sample.screens.menu.Menu
import br.com.leandroferreira.app_sample.screens.messagelist.MessageListScreen
import br.com.leandroferreira.storyteller.VideoFrameConfig

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

    NavHost(navController = navController, startDestination = Destinations.MENU.id) {
        composable(Destinations.MENU.id) {
            Menu(navController::navigate)
        }

        composable(Destinations.ADD_HISTORY.id) {
            AddStoryScreen()
        }

        composable(Destinations.IMAGE_LIST.id) {
            ImageListScreen()
        }

        composable(Destinations.MESSAGE_LIST.id) {
            MessageListScreen()
        }
    }
}

enum class Destinations(val id: String) {
    MENU("menu"),
    ADD_HISTORY("add_story"),
    IMAGE_LIST("image_list"),
    MESSAGE_LIST("message_list")
}

