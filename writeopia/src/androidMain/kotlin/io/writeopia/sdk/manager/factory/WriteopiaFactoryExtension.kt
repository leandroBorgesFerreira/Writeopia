package io.writeopia.sdk.manager.factory

import io.writeopia.sdk.manager.WriteopiaManager
import kotlinx.coroutines.Dispatchers

fun WriteopiaManager.defaultAndroid() = WriteopiaManager(dispatcher = Dispatchers.IO)