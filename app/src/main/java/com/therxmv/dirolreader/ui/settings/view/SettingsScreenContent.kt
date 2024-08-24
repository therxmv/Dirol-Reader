package com.therxmv.dirolreader.ui.settings.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.therxmv.dirolreader.ui.settings.view.common.SwitchItem
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsContentData.ItemData
import kotlinx.collections.immutable.PersistentList

@Composable
fun SettingsScreenContent(
    screenPadding: PaddingValues,
    items: PersistentList<ItemData>,
) {
    LazyColumn(
        modifier = Modifier
            .padding(screenPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
    ) {
        items(items) { item ->
            when (item) {
                is ItemData.Switch -> {
                    SwitchItem(
                        text = stringResource(id = item.text),
                        isChecked = item.isChecked,
                        onCheckedChange = {
                            item.onChecked(it)
                        }
                    )
                }
            }
        }
    }
}