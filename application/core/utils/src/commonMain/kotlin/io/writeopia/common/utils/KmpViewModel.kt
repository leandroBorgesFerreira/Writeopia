package io.writeopia.common.utils

import kotlinx.coroutines.CoroutineScope

abstract class KmpViewModel {

    protected lateinit var coroutineScope: CoroutineScope

    open fun initCoroutine(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }
}
