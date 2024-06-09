package com.therxmv.dirolreader.ui.settings.viewmodel.utils

import androidx.annotation.StringRes

data class SettingsContentData(
    @StringRes val appBarTitle: Int,
    val items: List<ItemData>,
) {
    sealed class ItemData {
        data class Switch(
            @StringRes val text: Int,
            val isChecked: Boolean,
            val onChecked: (Boolean) -> Unit,
        ) : ItemData()
    }
}