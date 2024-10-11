package io.writeopia.auth.core.manager

import io.writeopia.auth.core.data.User
import io.writeopia.common.utils.ResultData

internal class MockAuthManager : AuthManager {

    override suspend fun getUser(): User = User.disconnectedUser()

    override suspend fun isLoggedIn(): ResultData<Boolean> = ResultData.Complete(false)

    override suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): ResultData<Boolean> = ResultData.Complete(false)

    override suspend fun signIn(email: String, password: String): ResultData<Boolean> =
        ResultData.Complete(false)

    override suspend fun logout(): ResultData<Boolean> = ResultData.Complete(true)
}
