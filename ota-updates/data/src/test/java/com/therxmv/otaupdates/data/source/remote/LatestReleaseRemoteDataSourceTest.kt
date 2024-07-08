package com.therxmv.otaupdates.data.source.remote

import com.therxmv.otaupdates.data.models.LatestReleaseJson
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LatestReleaseRemoteDataSourceTest {

    private val mockGithubApiService = mockk<GithubApiService> {
        coEvery { getLatestRelease(any(), any()) } returns LatestReleaseJson(
            version = "v1.0.0",
            changeLog = "changeLog",
            assets = emptyList(),
        )
    }

    private val systemUnderTest = LatestReleaseRemoteDataSource(
        apiService = mockGithubApiService,
    )

    @Test
    fun `returns latest release data`() = runTest {
        val result = systemUnderTest.getLatestRelease()

        coVerify { mockGithubApiService.getLatestRelease(any(), any()) }
        result.shouldBeInstanceOf<LatestReleaseJson>()
    }

    @Test
    fun `returns null when exception was thrown`() = runTest {
        coEvery { mockGithubApiService.getLatestRelease(any(), any()) } throws Exception()

        val result = systemUnderTest.getLatestRelease()

        result.shouldBeNull()
    }
}