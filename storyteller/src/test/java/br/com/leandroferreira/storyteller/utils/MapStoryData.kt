package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import java.util.UUID

object MapStoryData {

    fun imageGroup(): Map<Int, StoryUnit> = mapOf(
        0 to GroupStep(
            id = "0",
            type = "group_image",
            localPosition = 0,
            steps = listOf(
                StoryStep(
                    id = "1",
                    type = "image",
                    localPosition = 0,
                    parentId = "0"
                ),

                StoryStep(
                    id = "2",
                    type = "image",
                    localPosition = 0,
                    parentId = "0"
                ),

                StoryStep(
                    id = "3",
                    type = "image",
                    localPosition = 0,
                    parentId = "0"
                )
            )
        ),
        1 to StoryStep(
            id = "4",
            type = "image",
            localPosition = 1
        )
    )

    fun imageStepsList(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "1",
            type = "image",
            localPosition = 0
        ),
        1 to StoryStep(
            id = "2",
            type = "image",
            localPosition = 0
        ),
        3 to StoryStep(
            id = "3",
            type = "image",
            localPosition = 0
        ),
    )

    fun complexList(): Map<Int, List<StoryUnit>> = mapOf(
        0 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                localPosition = -2,
            )
        ),
        1 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                localPosition = 1,
            )
        ),
        2 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                localPosition = 2,
            ),
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                localPosition = 2,
            ),
            StoryStep(
                id = "2",
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                localPosition = 2,
            )
        ),
        3 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message",
                text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                localPosition = 2
            )
        ),
        4 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                localPosition = 3,
                title = "The hotel entrance"
            )
        ),
        5 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
                localPosition = 4
            )
        ),
        6 to listOf(
            StoryStep(
                id = "6",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
                localPosition = 6
            )
        )
    )
}
