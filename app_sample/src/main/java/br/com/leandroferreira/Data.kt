package br.com.leandroferreira

import android.content.Context
import br.com.storyteller.model.StoryStep

fun history(context: Context): Map<Int, StoryStep> =
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
                type = "video",
                path = "android.resource://${context.packageName}/${R.raw.video}",
                url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                localPosition = 5
            )
        )
        put(
            6,
            StoryStep(
                id = "6",
                type = "add_button",
                text = "And it was super awesome!!",
                localPosition = 6
            )
        )
    }
