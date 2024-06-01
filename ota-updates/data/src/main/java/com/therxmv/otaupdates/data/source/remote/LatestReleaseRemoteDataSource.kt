package com.therxmv.otaupdates.data.source.remote

import com.therxmv.common.GithubRepo.GITHUB_REPO
import com.therxmv.common.GithubRepo.GITHUB_USERNAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LatestReleaseRemoteDataSource @Inject constructor(
    private val apiService: GithubApiService,
) {
    suspend fun getLatestRelease() = withContext(Dispatchers.IO) {
        try {
            apiService.getLatestRelease(GITHUB_USERNAME, GITHUB_REPO)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}