package com.therxmv.otaupdates.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import com.therxmv.otaupdates.domain.usecase.DownloadUpdateUseCase
import com.therxmv.otaupdates.domain.usecase.GetLatestReleaseUseCase
import com.therxmv.otaupdates.presentation.viewmodel.utils.OtaUiEvent
import com.therxmv.otaupdates.presentation.viewmodel.utils.OtaUiState
import com.therxmv.otaupdates.presentation.viewmodel.utils.toDownloadState
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OtaViewModel @Inject constructor(
    private val getLatestReleaseUseCase: GetLatestReleaseUseCase,
    private val downloadUpdateUseCase: DownloadUpdateUseCase,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
    @Named("VersionCode") private val versionCode: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OtaUiState>(OtaUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    private lateinit var updatePrefsListener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        checkVersion()
    }

    fun onEvent(event: OtaUiEvent) {
        when (event) {
            is OtaUiEvent.DownloadUpdate -> downloadUpdate(event.updateModel)
            is OtaUiEvent.InstallUpdate -> installUpdate(event.context, event.updateModel)
        }
    }

    private fun checkIfUpdateFileExists(): Boolean {
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = downloads.listFiles()?.find { it.isFile && it.name.contains("Dirol-Reader") }

        return file != null
    }

    private fun checkIfUpdateDownloaded(updateModel: LatestReleaseModel) {
        _uiState.update {
            (appSharedPrefsRepository.isUpdateDownloaded && checkIfUpdateFileExists()).toDownloadState(updateModel)
        }
    }

    private fun setIsUpdateDownloadedListener(updateModel: LatestReleaseModel) {
        updatePrefsListener = appSharedPrefsRepository.isUpdateDownloadedChangeListener { isDownloaded ->
            _uiState.update { (isDownloaded && checkIfUpdateFileExists()).toDownloadState(updateModel) }
            appSharedPrefsRepository.unregisterChangeListener(updatePrefsListener)
        }

        appSharedPrefsRepository.registerChangeListener(updatePrefsListener)
    }

    private fun checkVersion() {
        viewModelScope.launch {
            getLatestReleaseUseCase()?.let { release ->
                val version = release.version.filter { it.isDigit() }.toInt()

                if (version > versionCode) {
                    checkIfUpdateDownloaded(release)
                } else {
                    _uiState.update { OtaUiState.NoUpdates }
                }
            }
        }
    }

    private fun downloadUpdate(updateModel: LatestReleaseModel?) {
        updateModel?.let { model ->
            downloadUpdateUseCase(model)

            _uiState.update { OtaUiState.Downloading(model) }

            setIsUpdateDownloadedListener(model)
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