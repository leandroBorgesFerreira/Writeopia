package io.storiesteller.auth.core.data

const val DISCONNECTED_USER_ID = "disconnected_user"

data class User(
    val id: String,
    val email: String,
    val name: String,
) {
    companion object {
        fun disconnectedUser(): User = User(id = DISCONNECTED_USER_ID, "", "")
    }
}