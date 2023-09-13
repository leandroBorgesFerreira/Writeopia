package io.writeopia.auth.core.data

import io.writeopia.utils_module.DISCONNECTED_USER_ID

data class User(
    val id: String,
    val email: String,
    val name: String,
) {
    companion object {
        fun disconnectedUser(): User = User(id = DISCONNECTED_USER_ID, "", "")
    }
}