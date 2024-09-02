package io.writeopia.utils_module

import kotlinx.coroutines.CoroutineScope

abstract class KmpViewModel {

    protected lateinit var coroutineScope: CoroutineScope

    open fun initCoroutine(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }
}
