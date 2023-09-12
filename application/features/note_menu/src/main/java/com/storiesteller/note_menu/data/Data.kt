package com.storiesteller.note_menu.data

import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.model.story.StoryTypes
import java.util.UUID

internal fun supermarketList(): Map<Int, StoryStep> = mapOf(
    0 to StoryStep(
        localId = UUID.randomUUID().toString(),
        type = StoryTypes.TITLE.type,
        text = "Supermarket List",
    ),
    1 to StoryStep(
        localId = UUID.randomUUID().toString(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Corn",
    ),
    2 to StoryStep(
        localId = UUID.randomUUID().toString(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Chicken",
    ),
    3 to StoryStep(
        localId = UUID.randomUUID().toString(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Olives",
    ),
    4 to StoryStep(
        localId = UUID.randomUUID().toString(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Soup",
    )
)

internal fun travelHistory(): Map<Int, StoryStep> =
    mapOf(
        0 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.TITLE.type,
            text = "Travel notes",
        ),
        1 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.GROUP_IMAGE.type,
            steps = listOf(
                StoryStep(
                    localId = UUID.randomUUID().toString(),
                    type = StoryTypes.IMAGE.type,
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                ),
                StoryStep(
                    localId = UUID.randomUUID().toString(),
                    type = StoryTypes.IMAGE.type,
                    url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
                ),
                StoryStep(
                    localId = UUID.randomUUID().toString(),
                    type = StoryTypes.IMAGE.type,
                    url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                )
            ),
        ),
        2 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.MESSAGE.type,
            text = "We arrived in Santiago!!",
        ),
        3 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.CHECK_ITEM.type,
            text = "We need to go to the Cafe!",
        ),
        4 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.CHECK_ITEM.type,
            text = "We need to have lots of fun!",
        ),
        5 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.CHECK_ITEM.type,
            text = "We need to lear some Spanish!",
        ),
        6 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.IMAGE.type,
            url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
            title = "The hotel entrance"
        ),
        7 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.MESSAGE_BOX.type,
            text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
        ),
        8 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.VIDEO.type,
//                path = "android.resource://${context.packageName}/${R.raw.video}",
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
        ),
        9 to StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.MESSAGE_BOX.type,
            text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
        ),
    )
