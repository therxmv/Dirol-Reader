package com.therxmv.dirolreader.ui.settings.viewmodel.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList

@Immutable
data class SettingsContentData(
    @StringRes val appBarTitle: Int,
    val items: PersistentList<ItemData>,
) {
    sealed class ItemData {
        data class Switch(
            @StringRes val text: Int,
            val isChecked: Boolean,
            val onChecked: (Boolean) -> Unit,
        ) : ItemData()
    }
}