package io.writeopia.sdk.utils.files

interface KmpClosable {

    fun start()

    fun close()
}

fun <T : KmpClosable> T.useKmp(func: (T) -> Unit) {
    start()
    func(this)
    close()
}
