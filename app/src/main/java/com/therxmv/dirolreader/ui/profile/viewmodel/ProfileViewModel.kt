package com.therxmv.dirolreader.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.common.Links
import com.therxmv.common.R
import com.therxmv.dirolreader.domain.usecase.ProfileViewModelUseCase
import com.therxmv.dirolreader.ui.navigation.Destination
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.AppBarState
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiSection
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiState
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val client: Client,
    private val useCases: ProfileViewModelUseCase,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableStateFlow<Event?>(null)
    val eventFlow = _eventFlow.asStateFlow()

    init {
        setUpAppBar()
    }

    fun signOut() {
        viewModelScope.launch {
            delay(1500) // for better user experience

            client.send(TdApi.LogOut()) {
                client.send(TdApi.Close()) {
                    _eventFlow.update { Event.SignedOut }
                }
            }
        }
    }

    private fun setUpAppBar() {
        viewModelScope.launch(ioDispatcher) {
            val user = useCases.getCurrentUser()
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

    private fun getProfileSections(): PersistentList<ProfileUiSection> =
        persistentListOf(
            getSettingsSection(),
            getAboutSection(),
        )

    private fun getSettingsSection() = ProfileUiSection(
        title = R.string.profile_settings,
        items = persistentListOf(
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
        items = persistentListOf(
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

    sealed interface Event {
        data object SignedOut : Event
    }
}