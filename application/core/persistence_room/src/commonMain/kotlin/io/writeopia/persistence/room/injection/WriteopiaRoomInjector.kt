package io.writeopia.persistence.room.injection

import io.writeopia.persistence.room.WriteopiaApplicationDatabase

class WriteopiaRoomInjector private constructor(val database: WriteopiaApplicationDatabase){

    companion object {
        private var instance: WriteopiaRoomInjector? = null

        fun init(database: WriteopiaApplicationDatabase) {
            instance = WriteopiaRoomInjector(database)
        }

        fun get() = instance ?: throw IllegalStateException("WriteopiaRoomInjector not initialized!")
    }
}
