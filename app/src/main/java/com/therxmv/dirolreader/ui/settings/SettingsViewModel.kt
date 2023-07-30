package com.therxmv.dirolreader.ui.settings

import androidx.lifecycle.ViewModel
import com.therxmv.dirolreader.data.repository.AppSharedPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val appSharedPrefsRepository: AppSharedPrefsRepository
) : ViewModel() {

}