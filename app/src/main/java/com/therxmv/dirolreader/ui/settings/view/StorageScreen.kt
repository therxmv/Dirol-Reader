package com.therxmv.dirolreader.ui.settings.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun StorageScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenteredTopBar(
                title = stringResource(id = R.string.profile_storage),
                navController = navController,
            )
        }
    ) { padding ->
        StorageContent(
            screenPadding = padding,
            isAutoDeleteEnabled = viewModel.getIsAutoDeleteEnabled(),
            setAutoDelete = {
                viewModel.setIsAutoDeleteEnabled(it)
            }
        )
    }
}

@Composable
private fun StorageContent(
    screenPadding: PaddingValues,
    isAutoDeleteEnabled: Boolean,
    setAutoDelete: (Boolean) -> Unit,
) {
    // TODO some states(map/list) in VM for more items. And provide list of them from VM
    var isChecked by remember { mutableStateOf(isAutoDeleteEnabled) }

    Column(
        modifier = Modifier
            .padding(screenPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
    ) {
        SwitchItem(
            text = stringResource(id = R.string.settings_clear_cache),
            isChecked = isChecked,
            onCheckedChange = {
                setAutoDelete(it)
                isChecked = it
            }
        )
    }
}