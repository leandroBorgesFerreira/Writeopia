package io.writeopia.sdk.serialization.data

import kotlinx.serialization.Serializable

@Serializable
data class DocumentLinkApi(val id: String, val title: String? = null)
