package com.github.leandroborgesferreira.storyteller.serialization.request

import kotlinx.serialization.Serializable

@Serializable
data class Request<T>(val data: T)

fun <T> T.wrapInRequest() = Request(this)
