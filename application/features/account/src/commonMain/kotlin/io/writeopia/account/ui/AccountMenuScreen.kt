package io.writeopia.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.writeopia.account.viewmodel.AccountMenuViewModel
import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.toBoolean
import io.writeopia.model.ColorThemeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// import io.writeopia.appresourcers.R

@Composable
fun AccountMenuScreen(
    accountMenuViewModel: AccountMenuViewModel,
    isLoggedInState: StateFlow<ResultData<Boolean>>,
    onLogout: () -> Unit,
    goToRegister: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SettingsScreen(
            showPath = false,
            showOllamaConfig = false,
            selectedThemePosition = MutableStateFlow(0),
            selectColorTheme = selectColorTheme,
            workplacePathState = MutableStateFlow(""),
            selectWorkplacePath = {},
            ollamaAvailableModels = MutableStateFlow(ResultData.Idle()),
            ollamaUrlState = MutableStateFlow(""),
            ollamaSelectedModel = MutableStateFlow(""),
            downloadModelState = MutableStateFlow(ResultData.Idle()),
            ollamaUrlChange = {},
            ollamaModelChange = {},
            ollamaModelsRetry = {},
            downloadModel = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

//        Connect(accountMenuViewModel, isLoggedInState, onLogout, goToRegister)
    }
}

@Composable
private fun Connect(
    accountMenuViewModel: AccountMenuViewModel,
    isLoggedInState: StateFlow<ResultData<Boolean>>,
    onLogout: () -> Unit,
    goToRegister: () -> Unit
) {
    val isLoggedIn = isLoggedInState.collectAsState().value.toBoolean()

    Text(
        modifier = Modifier
            .clickable {
                if (isLoggedIn) {
                    accountMenuViewModel.logout(onLogout)
                } else {
                    accountMenuViewModel.eraseOfflineByChoice(goToRegister)
                }
            }
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(8.dp),
        text = if (isLoggedIn) "Logout" else "Register",
//            if (isLoggedIn) stringResource(R.string.logout) else stringResource(R.string.register),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold
        )
    )
}
