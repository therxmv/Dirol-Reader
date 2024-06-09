package com.therxmv.dirolreader.ui.settings.viewmodel

import com.therxmv.common.R
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsContentData
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Not a real ViewModel and can be used only inside real one
 */
class StorageViewModel @Inject constructor(
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) {
    private val _data = MutableStateFlow<SettingsContentData?>(null)
    val data = _data.asStateFlow()

    fun loadData() {
        _data.update {
            SettingsContentData(
                appBarTitle = R.string.profile_storage,
                items = listOf(
                    SettingsContentData.ItemData.Switch(
                        text = R.string.settings_clear_cache,
                        isChecked = appSharedPrefsRepository.isAutoDeleteEnabled,
                        onChecked = {
                            appSharedPrefsRepository.isAutoDeleteEnabled = it
                        }
                    )
                )
            )
        }
    }
}