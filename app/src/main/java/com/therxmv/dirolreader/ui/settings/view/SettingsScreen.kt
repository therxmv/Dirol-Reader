package com.therxmv.dirolreader.ui.settings.view

import androidx.compose.animation.Crossfade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.therxmv.dirolreader.ui.commonview.CenteredBoxLoader
import com.therxmv.dirolreader.ui.commonview.CenteredTopBar
import com.therxmv.dirolreader.ui.commonview.DefaultTitle
import com.therxmv.dirolreader.ui.settings.viewmodel.SettingsViewModel
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            (uiState as? SettingsUiState.Ready)?.data?.let {
                CenteredTopBar(
                    title = {
                        DefaultTitle(title = stringResource(id = it.appBarTitle))
                    },
                    navController = navController,
                )
            }
        }
    ) { padding ->
        Crossfade(targetState = uiState, label = "content") {
            when (it) {
                is SettingsUiState.Ready -> SettingsScreenContent(
                    screenPadding = padding,
                    items = it.data.items,
                )

                is SettingsUiState.Loading -> CenteredBoxLoader()
            }
        }
    }
}