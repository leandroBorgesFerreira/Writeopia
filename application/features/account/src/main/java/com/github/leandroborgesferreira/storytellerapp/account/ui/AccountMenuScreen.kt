package com.github.leandroborgesferreira.storytellerapp.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storytellerapp.account.viewmodel.AccountMenuViewModel

@Preview
@Composable
//Todo: Move AccountMenuViewModel constructor to injector!
fun AccountMenuScreen(
    accountMenuViewModel: AccountMenuViewModel,
    onLogoutSuccess: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    accountMenuViewModel.logout(onLogoutSuccess)
                }
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(8.dp),
            text = "Logout",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}