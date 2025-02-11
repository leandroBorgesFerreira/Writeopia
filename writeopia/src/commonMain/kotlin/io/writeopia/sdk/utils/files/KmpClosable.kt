package io.writeopia.sdk.utils.files

/**
 * Closable interface for KMP
 */
interface KmpClosable {

    fun start()

    fun close()
}

/**
 * Version of use { T -> } for KMP
 */
fun <T : KmpClosable> T.useKmp(func: (T) -> Unit) {
    start()
    func(this)
    close()
}
