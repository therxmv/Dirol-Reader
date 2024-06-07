package com.therxmv.dirolreader.ui.settings.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.commonview.CenteredTopBar
import com.therxmv.dirolreader.ui.settings.view.common.SwitchItem
import com.therxmv.dirolreader.ui.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemingScreen(
    navController: NavController,
    switchDynamicTheme: (Boolean) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenteredTopBar(
                title = stringResource(id = R.string.profile_theme),
                navController = navController,
            )
        }
    ) { padding ->
        ThemingContent(
            screenPadding = padding,
            isDynamicTheme = viewModel.getIsDynamic(),
            setDynamicTheme = {
                switchDynamicTheme(it)
            }
        )
    }
}

@Composable
private fun ThemingContent(
    screenPadding: PaddingValues,
    isDynamicTheme: Boolean,
    setDynamicTheme: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(screenPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
    ) {
        SwitchItem(
            text = stringResource(id = R.string.settings_dynamic_theme),
            isChecked = isDynamicTheme,
            onCheckedChange = {
                setDynamicTheme(it)
            }
        )
    }
}