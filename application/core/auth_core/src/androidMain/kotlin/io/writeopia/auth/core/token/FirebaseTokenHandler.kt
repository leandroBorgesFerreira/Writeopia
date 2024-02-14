package io.writeopia.auth.core.token

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.writeopia.sdk.network.oauth.BearerTokenHandler
import kotlinx.coroutines.tasks.await

object FirebaseTokenHandler : BearerTokenHandler {

    override suspend fun getIdToken(): String? =
        Firebase.auth.currentUser?.getIdToken(false)?.await()?.token?.also {token ->
            Log.d("FirebaseTokenHandler", "token: $token")
        }

}