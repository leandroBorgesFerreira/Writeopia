package io.writeopia.account.viewmodel

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.common.utils.KmpViewModel
import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.toBoolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AccountMenuKmpViewModel(
    private val authManager: AuthManager,
    private val authRepository: AuthRepository,
): AccountMenuViewModel, KmpViewModel() {
    private val _isLoggedIn: MutableStateFlow<ResultData<Boolean>> by lazy { MutableStateFlow(
        ResultData.Idle()) }
    override val isLoggedIn: StateFlow<ResultData<Boolean>> by lazy { _isLoggedIn.asStateFlow() }

    override fun checkLoggedIn() {
        coroutineScope.launch {
            _isLoggedIn.value = ResultData.Loading()
            _isLoggedIn.value = authManager.isLoggedIn()
        }
    }

    override fun logout(onLogOutSuccess: () -> Unit) {
        coroutineScope.launch {
            val result = authManager.logout()

            if (result.toBoolean()) {
                onLogOutSuccess()
            }
        }
    }

    override fun eraseOfflineByChoice(navigateToRegister: () -> Unit) {
        coroutineScope.launch {
            authRepository.eraseUserChoiceOffline()
            navigateToRegister()
        }
    }
}
