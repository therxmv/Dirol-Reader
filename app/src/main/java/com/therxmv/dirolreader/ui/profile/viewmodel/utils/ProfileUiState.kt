package com.therxmv.dirolreader.ui.profile.viewmodel.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

sealed class ProfileUiState {

    @Stable
    data class Ready(
        val appBarState: AppBarState,
        val sections: List<ProfileUiSection>,
    ) : ProfileUiState()

    data object Loading : ProfileUiState()
}

data class AppBarState(
    val avatarPath: String,
    val userName: String,
)

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