package io.writeopia.sdk.serialization.extensions

import io.writeopia.sdk.models.story.Tag
import io.writeopia.sdk.models.story.TagInfo
import io.writeopia.sdk.serialization.data.TagInfoApi

fun TagInfoApi.toModel() = TagInfo(Tag.fromString(this.tag)!!, this.position)

fun TagInfo.toApi() = TagInfoApi(this.tag.label, this.position)
