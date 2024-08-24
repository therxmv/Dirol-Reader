package com.therxmv.dirolreader.ui.settings.viewmodel

import com.therxmv.common.R
import com.therxmv.dirolreader.ui.settings.viewmodel.utils.SettingsContentData
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Not a real ViewModel and can be used only inside real one
 */
class ThemingViewModel @Inject constructor(
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) {
    private val _data = MutableStateFlow<SettingsContentData?>(null)
    val data = _data.asStateFlow()

    fun loadData(
        toggleDynamicTheme: (Boolean) -> Unit
    ) {
        _data.update {
            SettingsContentData(
                appBarTitle = R.string.profile_theme,
                items = persistentListOf(
                    SettingsContentData.ItemData.Switch(
                        text = R.string.settings_dynamic_theme,
                        isChecked = appSharedPrefsRepository.isDynamic,
                        onChecked = {
                            toggleDynamicTheme(it)
                            appSharedPrefsRepository.isDynamic = it
                        }
                    )
                )
            )
        }
    }
}