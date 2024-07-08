package com.therxmv.otaupdates.data.source.repository

import com.therxmv.otaupdates.data.models.LatestReleaseAssetJson
import com.therxmv.otaupdates.data.models.LatestReleaseJson
import com.therxmv.otaupdates.data.repository.LatestReleaseRepositoryImpl
import com.therxmv.otaupdates.data.source.remote.LatestReleaseRemoteDataSource
import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LatestReleaseRepositoryImplTest {

    private val releaseJson = LatestReleaseJson(
        version = "v1.0.0",
        changeLog = "changeLog",
        assets = listOf(
            LatestReleaseAssetJson(
                fileName = "name",
                contentType = "type",
                downloadUrl = "url",
            ),
        ),
    )
    private val mockLatestReleaseRemoteDataSource = mockk<LatestReleaseRemoteDataSource> {
        coEvery { getLatestRelease() } returns releaseJson
    }

    private val systemUnderTest = LatestReleaseRepositoryImpl(
        latestReleaseRemoteDataSource = mockLatestReleaseRemoteDataSource,
    )

    @Test
    fun `returns converted release model`() = runTest {
        val expectedModel = LatestReleaseModel(
            version = releaseJson.version,
            changeLog = releaseJson.changeLog,
            fileName = releaseJson.assets.first().fileName,
            contentType = releaseJson.assets.first().contentType,
            downloadUrl = releaseJson.assets.first().downloadUrl,
        )

        val result = systemUnderTest.getLatestRelease()

        result shouldBe expectedModel
    }
}