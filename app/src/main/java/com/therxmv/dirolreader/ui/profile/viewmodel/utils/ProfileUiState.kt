package com.therxmv.dirolreader.ui.profile.viewmodel.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList

sealed class ProfileUiState {

    @Immutable
    data class Ready(
        val appBarState: AppBarState,
        val sections: PersistentList<ProfileUiSection>,
    ) : ProfileUiState()

    data object Loading : ProfileUiState()
}

data class AppBarState(
    val avatarPath: String,
    val userName: String,
)

@Immutable
data class ProfileUiSection(
    @StringRes val title: Int,
    val items: PersistentList<Item>,
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