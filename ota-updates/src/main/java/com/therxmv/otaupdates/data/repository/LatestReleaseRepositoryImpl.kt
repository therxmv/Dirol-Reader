package com.therxmv.otaupdates.data.repository

import com.therxmv.otaupdates.data.source.remote.LatestReleaseRemoteDataSource
import javax.inject.Inject

class LatestReleaseRepositoryImpl @Inject constructor(
    private val latestReleaseRemoteDataSource: LatestReleaseRemoteDataSource
): LatestReleaseRepository {
    override suspend fun getLatestRelease() = latestReleaseRemoteDataSource.getLatestRelease()
}