package br.com.leandroferreira.app_sample.data

import android.content.Context
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import java.util.UUID

fun syncHistory(context: Context): List<StoryUnit> =
    buildList {
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                localPosition = 0,
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                localPosition = 0,
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message",
                text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                localPosition = 2
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "check_item",
                text = "We need to go to the Cafe!",
                localPosition = 3
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "check_item",
                text = "We need to have lots of fun!",
                localPosition = 4
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "check_item",
                text = "",
                localPosition = 5
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                localPosition = 6,
                title = "The hotel entrance"
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message",
                text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
                localPosition = 7
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "video",
                path = "android.resource://${context.packageName}/${R.raw.video}",
                url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                localPosition = 8
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
                localPosition = 9
            )
        )
        add(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "add_button",
                text = "And it was super awesome!!",
                localPosition = 10
            )
        )
    }

fun messages(): List<StoryUnit> = buildList {
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "message",
            text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            localPosition = 2
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "message",
            text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
            localPosition = 4
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "message",
            text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                "We had to buy some Syn Cards to be able to communicate in the new country. ",
            localPosition = 6
        )
    )
}
fun images(): List<StoryUnit> = buildList {
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            localPosition = 1
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
            localPosition = 2,
            text = "1"
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
            localPosition = 3,
            text = "2"
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/949/400/400.jpg?hmac=lGa4vz7HqqRP0noWMCCKo6Klo_MaYg6WpUulORAoVQU",
            localPosition = 4,
            text = "3"
        )
    )
}
