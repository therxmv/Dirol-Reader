package com.therxmv.dirolreader.ui.ota

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.BuildConfig
import com.therxmv.dirolreader.data.repository.AppSharedPrefsRepository
import com.therxmv.dirolreader.ui.ota.utils.OtaUiEvent
import com.therxmv.dirolreader.ui.ota.utils.OtaUiEvent.DownloadUpdate
import com.therxmv.dirolreader.ui.ota.utils.OtaUiEvent.InstallUpdate
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.Downloading
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.InitialState
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.NoUpdates
import com.therxmv.dirolreader.ui.ota.utils.toDownloadState
import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import com.therxmv.otaupdates.domain.usecase.GetLatestReleaseUseCase
import com.therxmv.otaupdates.downloadmanager.Downloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OtaViewModel @Inject constructor(
    private val getLatestReleaseUseCase: GetLatestReleaseUseCase,
    private val latestReleaseDownloader: Downloader,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OtaUiState>(InitialState)
    val uiState = _uiState.asStateFlow()

    var updateModel: LatestReleaseModel? = null

    private lateinit var updatePrefsListener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        checkVersion()
    }

    fun onEvent(event: OtaUiEvent) {
        when (event) {
            is DownloadUpdate -> downloadUpdate()
            is InstallUpdate -> installUpdate(event.context, updateModel)
        }
    }

    private fun checkIfUpdateFileExists(): Boolean {
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = downloads.listFiles()?.find { it.isFile && it.name.contains("Dirol-Reader") }

        return file != null
    }

    private fun checkIfUpdateDownloaded() {
        _uiState.update {
            (appSharedPrefsRepository.isUpdateDownloaded && checkIfUpdateFileExists()).toDownloadState()
        }
    }

    private fun setIsUpdateDownloadedListener() {
        updatePrefsListener = appSharedPrefsRepository.isUpdateDownloadedChangeListener { isDownloaded ->
            _uiState.update { (isDownloaded && checkIfUpdateFileExists()).toDownloadState() }
            appSharedPrefsRepository.unregisterChangeListener(updatePrefsListener)
        }

        appSharedPrefsRepository.registerChangeListener(updatePrefsListener)
    }

    private fun checkVersion() {
        viewModelScope.launch {
            getLatestReleaseUseCase.invoke()?.let { release ->
                val version = release.version.filter { it.isDigit() }.toInt()

                if (version > BuildConfig.VERSION_CODE) {
                    updateModel = release
                    checkIfUpdateDownloaded()
                } else {
                    _uiState.update { NoUpdates }
                }
            }
        }
    }

    private fun downloadUpdate() {
        updateModel?.let {
            latestReleaseDownloader.downloadFile(it)

            _uiState.update { Downloading }

            setIsUpdateDownloadedListener()
        }
    }

    private fun installUpdate(context: Context, update: LatestReleaseModel?) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, update?.fileName.orEmpty())

        if (file.exists()) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FileProvider.getUriForFile(context, "${context.applicationContext.packageName}.provider", file)
            } else {
                Uri.fromFile(file)
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, update?.contentType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}