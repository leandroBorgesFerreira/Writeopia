package io.writeopia.sdk.serialization.extensions

import io.writeopia.sdk.models.link.DocumentLink
import io.writeopia.sdk.serialization.data.DocumentLinkApi

fun DocumentLinkApi.toModel() = DocumentLink(
    id = this.id,
    title = this.title,
)

fun DocumentLink.toApi() = DocumentLinkApi(
    id = this.id,
    title = this.title,
)
