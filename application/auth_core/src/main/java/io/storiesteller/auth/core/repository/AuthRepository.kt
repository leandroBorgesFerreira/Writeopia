package io.storiesteller.auth.core.repository

import android.content.SharedPreferences
import io.storiesteller.auth.core.utils.USER_OFFLINE

class AuthRepository(private val sharedPreferences: SharedPreferences) {

    fun saveUserChoiceOffline() {
        sharedPreferences.edit().putBoolean(USER_OFFLINE, true).apply()
    }

    fun eraseUserChoiceOffline() {
        sharedPreferences.edit().putBoolean(USER_OFFLINE, false).commit()
    }

    fun isUserOfflineByChoice(): Boolean = sharedPreferences.getBoolean(USER_OFFLINE, false)
}