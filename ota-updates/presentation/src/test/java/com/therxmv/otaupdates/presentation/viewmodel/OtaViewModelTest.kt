package com.therxmv.otaupdates.presentation.viewmodel

import android.os.Environment
import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import com.therxmv.otaupdates.domain.usecase.DownloadUpdateUseCase
import com.therxmv.otaupdates.domain.usecase.GetLatestReleaseUseCase
import com.therxmv.otaupdates.presentation.viewmodel.utils.OtaUiState
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class OtaViewModelTest {

    private val releaseModel = LatestReleaseModel(
        version = "v1.0.0",
        changeLog = "changeLog",
        fileName = "fileName",
        contentType = "contentType",
        downloadUrl = "downloadUrl",
    )
    private val mockGetLatestReleaseUseCase = mockk<GetLatestReleaseUseCase> {
        coEvery { this@mockk.invoke() } returns null
    }
    private val mockDownloadUpdateUseCase = mockk<DownloadUpdateUseCase>(relaxed = true)
    private val mockAppSharedPrefsRepository = mockk<AppSharedPrefsRepository>(relaxed = true)
    private val mockVersionCode = 200

    private lateinit var systemUnderTest: OtaViewModel

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @Rule
    var rootFolder: TemporaryFolder = TemporaryFolder()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        assumeViewModelCreated()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `InitialState on viewModel init`() = runTest {
        systemUnderTest.uiState.value.shouldBeInstanceOf<OtaUiState.InitialState>()
    }

    @Test
    fun `set DownloadUpdate when release version is higher then installed`() = runTest {
        coEvery { mockGetLatestReleaseUseCase.invoke() } returns releaseModel.copy(version = "v5.0.0")

        advanceUntilIdle()

        coVerify { mockGetLatestReleaseUseCase.invoke() }
        systemUnderTest.uiState.value.shouldBeInstanceOf<OtaUiState.DownloadUpdate>()
    }

    @Test
    fun `set Downloaded when update apk file exists`() = runTest {
        mockkStatic(Environment::getExternalStoragePublicDirectory)

        rootFolder.newFile("Dirol-Reader-5.0.0.apk")
        every { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) } returns rootFolder.root
        every { mockAppSharedPrefsRepository.isUpdateDownloaded } returns true
        coEvery { mockGetLatestReleaseUseCase.invoke() } returns releaseModel.copy(version = "v5.0.0", fileName = "Dirol-Reader-5.0.0.apk")

        advanceUntilIdle()

        coVerify { mockGetLatestReleaseUseCase.invoke() }
        systemUnderTest.uiState.value.shouldBeInstanceOf<OtaUiState.Downloaded>()
    }

    @Test
    fun `set DownloadUpdate when different update apk file exists`() = runTest {
        mockkStatic(Environment::getExternalStoragePublicDirectory)

        rootFolder.newFile("Dirol-Reader-2.0.0.apk")
        every { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) } returns rootFolder.root
        every { mockAppSharedPrefsRepository.isUpdateDownloaded } returns true
        coEvery { mockGetLatestReleaseUseCase.invoke() } returns releaseModel.copy(version = "v5.0.0", fileName = "Dirol-Reader-5.0.0.apk")

        advanceUntilIdle()

        coVerify { mockGetLatestReleaseUseCase.invoke() }
        systemUnderTest.uiState.value.shouldBeInstanceOf<OtaUiState.DownloadUpdate>()
    }

    private fun assumeViewModelCreated() {
        systemUnderTest = OtaViewModel(
            getLatestReleaseUseCase = mockGetLatestReleaseUseCase,
            downloadUpdateUseCase = mockDownloadUpdateUseCase,
            appSharedPrefsRepository = mockAppSharedPrefsRepository,
            versionCode = mockVersionCode,
            ioDispatcher = testDispatcher,
        )
    }
}