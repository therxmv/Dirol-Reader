package com.therxmv.dirolreader.ui.settings

import androidx.lifecycle.ViewModel
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) : ViewModel() {

    fun getIsAutoDeleteEnabled() = appSharedPrefsRepository.isAutoDeleteEnabled

    fun setIsAutoDeleteEnabled(value: Boolean) {
        appSharedPrefsRepository.isAutoDeleteEnabled = value
    }

    fun getIsDynamic() = appSharedPrefsRepository.isDynamic
}