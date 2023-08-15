package com.therxmv.otaupdates.data.source.remote

import com.therxmv.otaupdates.utils.GithubRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LatestReleaseRemoteDataSource @Inject constructor(
    private val apiService: GithubApiService
) {
    suspend fun getLatestRelease() = withContext(Dispatchers.IO) {
        try {
            apiService.getLatestRelease(GithubRepo.GITHUB_USERNAME, GithubRepo.GITHUB_REPO)
        }
        catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}