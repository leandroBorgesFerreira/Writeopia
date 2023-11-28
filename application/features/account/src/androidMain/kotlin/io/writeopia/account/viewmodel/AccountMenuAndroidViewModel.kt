package io.writeopia.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

internal class AccountMenuAndroidViewModel(
    accountMenuKmpViewModel: AccountMenuKmpViewModel
) : ViewModel(), AccountMenuViewModel by accountMenuKmpViewModel {

    init {
        accountMenuKmpViewModel.initCoroutine(viewModelScope)
    }
}