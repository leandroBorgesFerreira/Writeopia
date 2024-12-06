package io.writeopia.common.utils.collections

fun <R> Iterable<R>.inBatches(batchSize: Int = 2): List<List<R>> {
    require(batchSize > 0) { "Batch size must be greater than 0." }

    val iterator = this.iterator()

    return buildList {
        while (iterator.hasNext()) {
            val batch = mutableListOf<R>()

            repeat(batchSize) {
                if (iterator.hasNext()) {
                    batch.add(iterator.next())
                }
            }
            this.add(batch)
        }
    }
}
