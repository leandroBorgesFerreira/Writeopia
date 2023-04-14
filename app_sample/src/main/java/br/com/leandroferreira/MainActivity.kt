package br.com.leandroferreira

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.storyteller.StoryTellerTimeline
import br.com.storyteller.defaultDrawers
import br.com.storyteller.model.StoryStep

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var history by remember { mutableStateOf(history()) }

            Box(modifier = Modifier.padding(10.dp)) {
                StoryTellerTimeline(
                    modifier = Modifier.width(400.dp),
                    steps = history,
                    drawers = defaultDrawers(onCommand = { command ->
                        when (command.type) {
                            "move_up" -> {}
                            "move_down" -> {}
                            "delete" -> {
                                Log.d("OnDelete", "deleting...")
                                history = (history.toSet() - command.step).toList()
                            }
                        }
                    })
                )
            }
        }
    }
}

private fun history(): List<StoryStep> =
    buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE"
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "message",
                text = "We arrived in Santiago!!"
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                url = "https://fastly.picsum.photos/id/586/400/400.jpg?hmac=cwCJngku1FJAlm3jB_5APROv6ftBlPlCZnrdXU-iAac"
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "message",
                text = "And it was super awesome!!"
            )
        )
        add(
            StoryStep(
                id = "5",
                type = "add_button",
                text = "And it was super awesome!!"
            )
        )
    }
