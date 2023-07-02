package com.therxmv.dirolreader.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.therxmv.dirolreader.data.repository.AppSharedPrefsRepository
import com.therxmv.dirolreader.utils.FILES_PATH
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val appSharedPrefsRepository: AppSharedPrefsRepository
) : ViewModel() {

}