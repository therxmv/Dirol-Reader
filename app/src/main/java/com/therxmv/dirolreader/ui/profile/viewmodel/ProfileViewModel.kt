package com.therxmv.dirolreader.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.common.Links
import com.therxmv.common.R
import com.therxmv.dirolreader.domain.usecase.ProfileViewModelUseCase
import com.therxmv.dirolreader.ui.navigation.Destination
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.AppBarState
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiEvent
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiSection
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiState
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: ProfileViewModelUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var client: Client? = useCases.getClientUseCase()

    init {
        setUpAppBar()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.LogOut -> {
                logOut(event.onComplete)
            }
        }
    }

    private fun logOut(onComplete: () -> Unit) {
        client?.send(TdApi.LogOut()) {}
        client?.close()
        onComplete()
    }

    private fun setUpAppBar() {
        viewModelScope.launch {
            val user = useCases.getCurrentUserUseCase(client)
            val appBar = AppBarState(
                avatarPath = user.avatarPath,
                userName = "${user.firstName} ${user.lastName}",
            )

            _uiState.update {
                ProfileUiState.Ready(
                    appBarState = appBar,
                    sections = getProfileSections(),
                )
            }
        }
    }

    private fun getProfileSections(): List<ProfileUiSection> =
        listOf(
            getSettingsSection(),
            getAboutSection(),
        )

    private fun getSettingsSection() = ProfileUiSection(
        title = R.string.profile_settings,
        items = listOf(
            ProfileUiSection.Item(
                icon = R.drawable.theme_icon,
                name = R.string.profile_theme,
                onClick = ProfileUiSection.ItemClick.Navigate(
                    Destination.SettingsScreen.createRoute(SettingsScreens.THEMING.name)
                ),
            ),
            ProfileUiSection.Item(
                icon = R.drawable.storage_icon,
                name = R.string.profile_storage,
                onClick = ProfileUiSection.ItemClick.Navigate(
                    Destination.SettingsScreen.createRoute(SettingsScreens.STORAGE.name)
                )
            ),
        ),
    )

    private fun getAboutSection() = ProfileUiSection(
        title = R.string.profile_about,
        items = listOf(
            ProfileUiSection.Item(
                icon = R.drawable.telegram_icon,
                name = R.string.profile_telegram,
                onClick = ProfileUiSection.ItemClick.OpenBrowser(Links.TELEGRAM_CHANNEL)
            ),
            ProfileUiSection.Item(
                icon = R.drawable.github_icon,
                name = R.string.profile_github,
                onClick = ProfileUiSection.ItemClick.OpenBrowser(Links.GITHUB_REPO)
            ),
        ),
    )
}