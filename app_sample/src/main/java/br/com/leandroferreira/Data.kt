package br.com.leandroferreira

import android.content.Context
import br.com.storyteller.model.StoryStep

fun history(context: Context): List<StoryStep> =
    buildList {
        add(
            StoryStep(
                id = "0",
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "1",
                type = "image",
                url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
                localPosition = 1
            )
        )
        add(

            StoryStep(
                id = "2",
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                localPosition = 2
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "message",
                text = "We arrived in Santiago!!",
                localPosition = 2
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                url = "https://fastly.picsum.photos/id/586/400/400.jpg?hmac=cwCJngku1FJAlm3jB_5APROv6ftBlPlCZnrdXU-iAac",
                localPosition = 3
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "message",
                text = "And it was super awesome!!",
                localPosition = 4
            )
        )
        add(
            StoryStep(
                id = "5",
                type = "video",
                path = "android.resource://${context.packageName}/${R.raw.video}",
                url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                localPosition = 5
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
        add(
            StoryStep(
                id = "7",
                type = "add_button",
                text = "And it was super awesome!!",
                localPosition = 7
            )
        )
    }
