package br.com.leandroferreira.app_sample.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.leandroferreira.app_sample.screens.addstory.AddStoryScreen
import br.com.leandroferreira.app_sample.screens.addstory.StoriesRepo
import br.com.leandroferreira.app_sample.viewmodel.StoriesViewModel
import br.com.leandroferreira.storyteller.VideoFrameConfig
import br.com.leandroferreira.storyteller.manager.StoryTellerManager

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

    NavHost(navController = navController, startDestination = Destinations.ADD_HISTORY.id) {
        composable(Destinations.ADD_HISTORY.id) {
            val repo = StoriesRepo(LocalContext.current)
            val storyTellerManager = StoryTellerManager()
            val storiesViewModel: StoriesViewModel = viewModel(initializer = {
                StoriesViewModel(storyTellerManager, repo)
            })

            AddStoryScreen(storiesViewModel)
        }
    }
}

enum class Destinations(val id: String) {
    ADD_HISTORY("add_story"),
}

