package br.com.leandroferreira

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.storyteller.StoryTellerTimeline
import br.com.storyteller.model.StoryStep

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(modifier = Modifier.padding(10.dp)) {
                StoryTellerTimeline(modifier = Modifier.width(400.dp), steps = history())
            }
        }
    }
}

private fun history(): List<StoryStep> =
    buildList {
        add(
            StoryStep(
                id = "1",
                type = "message",
                text = "We arrived in Santiago!!"
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "message",
                text = "And it was super awesome!!"
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "add_button",
                text = "And it was super awesome!!"
            )
        )
    }

@Preview
@Composable
fun DefaultPreview() {
    StoryTellerTimeline(modifier = Modifier.width(400.dp), steps = history())
}
