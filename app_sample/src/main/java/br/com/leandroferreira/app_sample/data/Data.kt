package br.com.leandroferreira.app_sample.data

import android.content.Context
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import java.util.UUID

fun messages(): List<StoryUnit> = buildList {
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "message_box",
            text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "message_box",
            text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "message_box",
            text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                "We had to buy some Syn Cards to be able to communicate in the new country. ",
        )
    )
}

fun messagesMap(): Map<Int, StoryUnit> =
    mapOf(
        0 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message_box",
                text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            ),
        1 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message_box",
                text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
            ),
        2 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message_box",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
            )
    )

fun images(): List<StoryUnit> = buildList {
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
            text = "1"
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
            text = "2"
        )
    )
    add(
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/949/400/400.jpg?hmac=lGa4vz7HqqRP0noWMCCKo6Klo_MaYg6WpUulORAoVQU",
            text = "3"
        )
    )
}

fun imagesMap(): Map<Int, StoryUnit> =
    mapOf(
        0 to StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
        ),
        1 to StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
            text = "1"
        ),
        2 to StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
            text = "2"
        ),
        3 to StoryStep(
            id = UUID.randomUUID().toString(),
            type = "image",
            url = "https://fastly.picsum.photos/id/949/400/400.jpg?hmac=lGa4vz7HqqRP0noWMCCKo6Klo_MaYg6WpUulORAoVQU",
            text = "3"
        )
    )

fun supermarketList(): Map<Int, StoryUnit> = mapOf(
    0 to StoryStep(
        id = UUID.randomUUID().toString(),
        type = "message",
        text = "To Buy:",
    ),
    1 to StoryStep(
        id = UUID.randomUUID().toString(),
        type = "check_item",
        text = "Corn",
    ),
    2 to StoryStep(
        id = UUID.randomUUID().toString(),
        type = "check_item",
        text = "Chicken",
    ),
    3 to StoryStep(
        id = UUID.randomUUID().toString(),
        type = "check_item",
        text = "Olives",
    ),
    4 to StoryStep(
        id = UUID.randomUUID().toString(),
        type = "check_item",
        text = "Soup",
    )
)

fun syncHistory(context: Context): Map<Int, StoryUnit> =
    mapOf(
        0 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            ),
        1 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
            ),
        2 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
            ),
        3 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message",
                text = "We arrived in Santiago!!",
            ),
        4 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "check_item",
                text = "We need to go to the Cafe!",
            ),
        5 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "check_item",
                text = "We need to have lots of fun!",
            ),
        6 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "check_item",
                text = "We need to lear some Spanish!",
            ),
        7 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                title = "The hotel entrance"
            ),
        8 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message_box",
                text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
            ),
        9 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "video",
                path = "android.resource://${context.packageName}/${R.raw.video}",
                url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            ),
        10 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message_box",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
            ),
        11 to
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "add_button",
                text = "And it was super awesome!!",
            )
    )
