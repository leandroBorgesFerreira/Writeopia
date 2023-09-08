package com.github.leandroborgesferreira.storytellerapp.auth.core

import android.content.SharedPreferences
import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.github.leandroborgesferreira.storytellerapp.auth.core.data.User
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData

private const val ANONYMOUS_USER_KEY = "ANONYMOUS_USER_KEY"

/**
 * This class encapsulates the logic of auth so the framework doesn't get exposed to the app, making
 * changes easier (like changing Amplify to Firebase)
 */
class AuthManager(private val sharedPreferences: SharedPreferences) {

    fun setUsageAsAnonymous(isAnonymous: Boolean) {
        sharedPreferences.edit().run {
            this.putBoolean(ANONYMOUS_USER_KEY, isAnonymous)
        }
    }

    suspend fun getUser(): User? =
        try {
            val userAttributes = Amplify.Auth.fetchUserAttributes()

            val user = User(
                name = userAttributes
                    .find { userAttribute -> userAttribute.key == AuthUserAttributeKey.name() }
                    ?.value ?: "",
                email = userAttributes
                    .find { userAttribute -> userAttribute.key == AuthUserAttributeKey.email() }
                    ?.value ?: "",
            )

            userAttributes
                .find { userAttribute -> userAttribute.key == AuthUserAttributeKey.name() }
                ?.value ?: ""

            user
        } catch (e: Exception) {
            null
        }

    suspend fun isLoggedIn(): ResultData<Boolean> =
        try {
            val session = Amplify.Auth.fetchAuthSession()
            Log.i("AmplifyQuickstart", "Auth session = $session")
            ResultData.Complete(session.isSignedIn)
        } catch (error: AuthException) {
            Log.e("AmplifyQuickstart", "Failed to fetch auth session", error)
            ResultData.Error(error)
        }

    suspend fun signUp(email: String, password: String, name: String): ResultData<Boolean> {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .userAttribute(AuthUserAttributeKey.name(), name)
            .build()

        return try {
            val result = Amplify.Auth.signUp(email, password, options)

            return if (result.isSignUpComplete) {
                Log.i("AuthQuickStart", "Result: $result")
                ResultData.Complete(true)
            } else {
                ResultData.Complete(false)
            }
        } catch (error: Exception) {
            Log.e("AuthQuickStart", "Sign up failed", error)
            ResultData.Error(error)
        }
    }

    suspend fun signIn(email: String, password: String): ResultData<Boolean> =
        try {
            val result = Amplify.Auth.signIn(email, password)
            if (result.isSignedIn) {
                val session = Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
                setUsageAsAnonymous(false)
                Log.i(
                    "AuthQuickstart",
                    "Sign in succeeded. Token: ${session.userPoolTokensResult.value?.idToken}"
                )

            } else {
                Log.e("AuthQuickstart", "Sign in not complete")
            }

            ResultData.Complete(result.isSignedIn)
        } catch (error: Exception) {
            Log.e("AuthQuickstart", "Sign in failed", error)
            ResultData.Error(error)
        }

    suspend fun logout(): ResultData<Boolean> =
        when (val signOutResult = Amplify.Auth.signOut()) {
            is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                // Sign Out completed fully and without errors.
                Log.e("AuthQuickStart", "Logout!")
                ResultData.Complete(true)
            }

            is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                // Sign Out completed with some errors. User is signed out of the device.
                signOutResult.hostedUIError?.let {
                    Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                    // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.
                }
                signOutResult.globalSignOutError?.let {
                    Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.accessToken.
                }
                signOutResult.revokeTokenError?.let {
                    Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.refreshToken.
                }

                ResultData.Complete(false)
            }

            is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                // Sign Out failed with an exception, leaving the user signed in.
                Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
                ResultData.Error(signOutResult.exception)
            }

            else -> ResultData.Complete(false)
        }
}