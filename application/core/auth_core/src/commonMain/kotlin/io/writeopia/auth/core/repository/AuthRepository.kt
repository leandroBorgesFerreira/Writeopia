package io.writeopia.auth.core.repository

interface AuthRepository {

    fun saveUserChoiceOffline()

    fun eraseUserChoiceOffline()

    fun isUserOfflineByChoice(): Boolean
}