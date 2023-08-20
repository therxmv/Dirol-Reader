package com.therxmv.dirolreader.ui.ota

import android.content.SharedPreferences
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.BuildConfig
import com.therxmv.dirolreader.data.repository.AppSharedPrefsRepository
import com.therxmv.dirolreader.ui.ota.utils.DownloadState
import com.therxmv.dirolreader.ui.ota.utils.OtaUiEvent
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState
import com.therxmv.otaupdates.domain.usecase.GetLatestReleaseUseCase
import com.therxmv.otaupdates.downloadmanager.Downloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtaViewModel @Inject constructor(
    private val getLatestReleaseUseCase: GetLatestReleaseUseCase,
    private val latestReleaseDownloader: Downloader,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
): ViewModel() {

    private val _state = MutableStateFlow(OtaUiState())
    val state = _state.asStateFlow()

    private lateinit var updatePrefsListener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        checkVersion()
        checkIfUpdateDownloaded()
    }

    fun onEvent(event: OtaUiEvent) {
        when(event) {
            is OtaUiEvent.DownloadUpdate -> {
                _state.value.updateModel?.let {
                    latestReleaseDownloader.downloadFile(it)
                    _state.value = _state.value.copy(
                        downloadState = DownloadState.DOWNLOADING
                    )
                    setIsUpdateDownloadedListener()
                }
            }
        }
    }

    private fun checkIfUpdateFileExists(): Boolean {
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = downloads.listFiles()?.firstOrNull { it.isFile && it.name.contains("Dirol-Reader") }

        return file != null
    }

    private fun checkIfUpdateDownloaded() {
        if(appSharedPrefsRepository.isUpdateDownloaded && checkIfUpdateFileExists()) {
            _state.value = _state.value.copy(
                downloadState = DownloadState.DOWNLOADED
            )
        }
        else {
            _state.value = _state.value.copy(
                downloadState = DownloadState.DOWNLOAD
            )
        }
    }

    private fun setIsUpdateDownloadedListener() {
        updatePrefsListener = appSharedPrefsRepository.isUpdateDownloadedChangeListener {
            if(it && checkIfUpdateFileExists()) {
                _state.value = _state.value.copy(
                    downloadState = DownloadState.DOWNLOADED
                )
                appSharedPrefsRepository.unregisterChangeListener(updatePrefsListener)
            }
            else {
                _state.value = _state.value.copy(
                    downloadState = DownloadState.DOWNLOAD
                )
                appSharedPrefsRepository.unregisterChangeListener(updatePrefsListener)
            }
        }

        appSharedPrefsRepository.registerChangeListener(updatePrefsListener)
    }

    private fun checkVersion() {
        viewModelScope.launch {
            getLatestReleaseUseCase.invoke()?.let { release ->
                val version = release.version.filter { it.isDigit() }.toInt()

                _state.value = _state.value.copy(
                    isUpdateAvailable = version > BuildConfig.VERSION_CODE,
                    updateModel = release,
                )
            }
        }
    }
}