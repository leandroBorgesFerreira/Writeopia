package io.writeopia.auth.core.manager

import io.writeopia.auth.core.data.User
import io.writeopia.utils_module.ResultData

interface AuthManager {

    suspend fun getUser(): User

    suspend fun isLoggedIn(): ResultData<Boolean>

    suspend fun signUp(email: String, password: String, name: String): ResultData<Boolean>

    suspend fun signIn(email: String, password: String): ResultData<Boolean>

    suspend fun logout(): ResultData<Boolean>
}