package br.com.storyteller.utils

import br.com.leandroferreira.storyteller.model.StoryStep

object StoryData {

    fun imageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 0
            )
        )
    }

    fun messageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "message",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "message",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "message",
                localPosition = 0
            )
        )
    }

    fun stepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "message",
                localPosition = 4
            )
        )
        add(
            StoryStep(
                id = "5",
                type = "message",
                localPosition = 5
            )
        )
    }

    fun complexList() =
        buildList {
            add(
                StoryStep(
                    id = "-2",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                    localPosition = -2,
                )
            )
            add(
                StoryStep(
                    id = "-1",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                    localPosition = -1,
                )
            )
            add(
                StoryStep(
                    id = "0",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                    localPosition = 0,
                )
            )
            add(
                StoryStep(
                    id = "1",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
                    localPosition = 0
                )
            )
            add(
                StoryStep(
                    id = "2",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                    localPosition = 0,
                )
            )
            add(
                StoryStep(
                    id = "2",
                    type = "message",
                    text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    localPosition = 2
                )
            )
            add(
                StoryStep(
                    id = "3",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                    localPosition = 3,
                    title = "The hotel entrance"
                )
            )
            add(
                StoryStep(
                    id = "4",
                    type = "message",
                    text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
                    localPosition = 4
                )
            )
            add(
                StoryStep(
                    id = "6",
                    type = "message",
                    text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                        "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                        "We had to buy some Syn Cards to be able to communicate in the new country. ",
                    localPosition = 6
                )
            )
        }

}
