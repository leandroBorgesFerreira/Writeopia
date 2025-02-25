package io.writeopia.sqldelight.di

import io.writeopia.sql.WriteopiaDb

class WriteopiaDbInjector private constructor(val database: WriteopiaDb) {

    companion object {
        private var instance: WriteopiaDbInjector? = null

        fun initialize(writeopiaDb: WriteopiaDb) {
            WriteopiaDbInjector(database = writeopiaDb)
        }

        fun singleton() = instance
    }

}
