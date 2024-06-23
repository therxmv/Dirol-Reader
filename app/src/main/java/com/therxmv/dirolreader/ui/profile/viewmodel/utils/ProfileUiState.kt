package com.therxmv.dirolreader.ui.profile.viewmodel.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.therxmv.dirolreader.ui.news.viewmodel.utils.ToolbarData

sealed class ProfileUiState {

    @Stable
    data class Ready(
        val appBarState: ToolbarData,
        val sections: List<ProfileUiSection>,
    ) : ProfileUiState()

    data object Loading : ProfileUiState()
}

@Stable
data class ProfileUiSection(
    @StringRes val title: Int,
    val items: List<Item>,
) {
    data class Item(
        @DrawableRes val icon: Int,
        @StringRes val name: Int,
        val onClick: ItemClick,
    )

    sealed class ItemClick {
        data class OpenBrowser(val link: String) : ItemClick()
        data class Navigate(val route: String) : ItemClick()
    }
}