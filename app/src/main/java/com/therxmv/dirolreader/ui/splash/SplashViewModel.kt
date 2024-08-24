package com.therxmv.dirolreader.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.common.Path
import com.therxmv.common.extractVersion
import com.therxmv.dirolreader.domain.usecase.GetTdLibParametersUseCase
import com.therxmv.dirolreader.ui.navigation.Destination
import com.therxmv.otaupdates.domain.usecase.GetLatestReleaseUseCase
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val client: Client,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
    private val getLatestRelease: GetLatestReleaseUseCase,
    private val getTdLibParameters: GetTdLibParametersUseCase,
    @Named("VersionCode") private val versionCode: Int,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _eventFlow = MutableStateFlow<Event?>(null)
    val eventFlow = _eventFlow.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            clearCache()
            val needToUpdate = isUpdateAvailable()
            authorizeClient(needToUpdate)
        }
    }

    private fun clearCache() {
        if (appSharedPrefsRepository.isAutoDeleteEnabled) {
            File(Path.FILES_PATH)
                .listFiles()
                ?.filter { file ->
                    file.isDirectory && file.listFiles()?.any { it.name.contains(".nomedia") } == true
                }
                ?.forEach { folder ->
                    folder.listFiles()?.forEach { elem ->
                        if (elem.name.contains(".nomedia").not()) {
                            elem.delete()
                        }
                    }
                }
        }
    }

    private suspend fun isUpdateAvailable(): Boolean =
        getLatestRelease()?.let { release ->
            val version = release.version.extractVersion()
            version > versionCode
        } == true

    private fun authorizeClient(needToUpdate: Boolean) {
        client.send(TdApi.GetAuthorizationState()) { state ->
            onAuthorizationStateUpdated(
                state = state as TdApi.AuthorizationState,
                needToUpdate = needToUpdate,
            )
        }
    }

    private fun onAuthorizationStateUpdated(state: TdApi.AuthorizationState, needToUpdate: Boolean) {
        when (state) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                Log.d("rozmi_auth_splash", "AuthState.PARAMS")
                client.send(getTdLibParameters()) {
                    authorizeClient(needToUpdate)
                }
            }

            is TdApi.AuthorizationStateReady -> {
                Log.d("rozmi_auth_splash", "AuthState.READY")
                _eventFlow.update {
                    resolveNavigationEvent(Destination.NewsScreen.route, needToUpdate)
                }
            }

            else -> {
                Log.d("rozmi_auth_splash", state.toString())
                _eventFlow.update {
                    resolveNavigationEvent(Destination.AuthScreen.route, needToUpdate)
                }
            }
        }
    }

    private fun resolveNavigationEvent(nextScreen: String, needToUpdate: Boolean): Event =
        if (needToUpdate) {
            Event.NavigateToNextScreen(Destination.OtaScreen.createRoute(nextScreen))
        } else {
            Event.NavigateToNextScreen(nextScreen)
        }

    sealed class Event {
        data class NavigateToNextScreen(val route: String) : Event()
    }
}