package io.writeopia.sdk.serialization.extensions

import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.serialization.data.IconApi

fun MenuItem.Icon.toApi() = IconApi(
    label = this.label,
    tint = this.tint
)

fun IconApi.toModel() = MenuItem.Icon(
    label = this.label,
    tint = this.tint
)
