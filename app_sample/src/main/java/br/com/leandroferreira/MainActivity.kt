package br.com.leandroferreira

import android.os.Bundle
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
                    steps = history.values.sorted(),
                    drawers = defaultDrawers(onCommand = { command ->
                        when (command.type) {
                            "move_up" -> {
                                history = moveUp(command.step.localPosition, history)
                            }
                            "move_down" -> {
                                history = moveDown(command.step.localPosition, history)
                            }
                            "delete" -> {
                                history = history - command.step.localPosition
                            }
                        }
                    })
                )
            }
        }
    }
}

private fun moveUp(position: Int, history: Map<Int, StoryStep>): Map<Int, StoryStep> {
    val thisStep = history[position]
    val upStep = history[position - 1]

    val mutableHistory = history.toMutableMap()
    upStep?.let { step ->
        mutableHistory[position] = step.copy(localPosition = position)
    }

    thisStep?.let { step ->
        mutableHistory[position - 1] =
            step.copy(localPosition = position - 1)
    }

    return mutableHistory.toMap()
}

private fun moveDown(position: Int, history: Map<Int, StoryStep>): Map<Int, StoryStep> {
    return moveUp(position + 1, history)
}

private fun history(): Map<Int, StoryStep> =
    buildMap {
        put(
            1,
            StoryStep(
                id = "1",
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                localPosition = 1
            )
        )
        put(
            2,
            StoryStep(
                id = "2",
                type = "message",
                text = "We arrived in Santiago!!",
                localPosition = 2
            )
        )
        put(
            3,
            StoryStep(
                id = "3",
                type = "image",
                url = "https://fastly.picsum.photos/id/586/400/400.jpg?hmac=cwCJngku1FJAlm3jB_5APROv6ftBlPlCZnrdXU-iAac",
                localPosition = 3
            )
        )
        put(
            4,
            StoryStep(
                id = "4",
                type = "message",
                text = "And it was super awesome!!",
                localPosition = 4
            )
        )
        put(
            5,
            StoryStep(
                id = "5",
                type = "add_button",
                text = "And it was super awesome!!",
                localPosition = 5
            )
        )
    }
