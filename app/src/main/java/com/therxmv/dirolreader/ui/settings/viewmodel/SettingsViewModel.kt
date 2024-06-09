package com.therxmv.dirolreader.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsContentData
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsScreens
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsUiState
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = SettingsViewModel.Factory::class)
class SettingsViewModel @AssistedInject constructor(
    @Assisted private val destination: String?,
    @Assisted private val toggleDynamicTheme: (Boolean) -> Unit,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
    private val storageViewModel: StorageViewModel,
    private val themingViewModel: ThemingViewModel,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(destination: String?, toggleDynamicTheme: (Boolean) -> Unit): SettingsViewModel
    }

    val uiState = combine(
        flow = themingViewModel.data,
        flow2 = storageViewModel.data,
        transform = ::createDataState,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SettingsUiState.Loading,
        )

    init {
        loadScreenData()
    }

    private fun createDataState(
        themingData: SettingsContentData?,
        storageData: SettingsContentData?,
    ): SettingsUiState =
        when (destination) {
            SettingsScreens.THEMING.name -> {
                themingData?.let {
                    SettingsUiState.Ready(it)
                }
            }

            else -> {
                storageData?.let {
                    SettingsUiState.Ready(it)
                }
            }
        } ?: SettingsUiState.Loading

    private fun loadScreenData() {
        when (destination) {
            SettingsScreens.THEMING.name -> themingViewModel.loadData(toggleDynamicTheme)
            SettingsScreens.STORAGE.name -> storageViewModel.loadData()
        }
    }

    fun getIsAutoDeleteEnabled() = appSharedPrefsRepository.isAutoDeleteEnabled

    fun setIsAutoDeleteEnabled(value: Boolean) {
        appSharedPrefsRepository.isAutoDeleteEnabled = value
    }

    fun getIsDynamic() = appSharedPrefsRepository.isDynamic
}