package br.com.leandroferreira

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.storyteller.StoryTellerTimeline
import br.com.storyteller.VideoFrameConfig
import br.com.storyteller.drawer.DefaultDrawers
import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.StepsNormalizationBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VideoFrameConfig.configCoilForVideoFrame(this)

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    val viewModel = HistoriesViewModel(
        StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }
    )

    var history by remember { mutableStateOf(viewModel.normalizedHistories(context)) }

    Box(modifier = Modifier.padding(10.dp)) {
        StoryTellerTimeline(
            modifier = Modifier.width(400.dp),
            story = history.values.sorted(),
            drawers = DefaultDrawers.create(onCommand = { command ->
                when (command.type) {
                    "move_up" -> {
                        history = moveUp(command.step.localPosition, history, viewModel)
                    }

                    "move_down" -> {
                        history = moveDown(command.step.localPosition, history, viewModel)
                    }

                    "delete" -> {
                        history = history - command.step.localPosition
                    }
                }
            })
        )
    }
}


private fun moveUp(
    position: Int,
    history: Map<Int, StoryUnit>,
    viewModel: HistoriesViewModel
): Map<Int, StoryUnit> {
    val thisStep = history[position]
    val upStep = history[position - 1]

    val mutableHistory = history.toMutableMap()
    upStep?.let { step ->
        mutableHistory[position] = step.copyWithNewPosition(position)
    }

    thisStep?.let { step ->
        mutableHistory[position - 1] =
            step.copyWithNewPosition(position - 1)
    }

    return mutableHistory.values
        .toList()
        .let(viewModel::normalizeHistories)
        .associateBy { it.localPosition }
}

private fun moveDown(
    position: Int,
    history: Map<Int, StoryUnit>,
    viewModel: HistoriesViewModel
): Map<Int, StoryUnit> {
    return moveUp(position + 1, history, viewModel)
}

