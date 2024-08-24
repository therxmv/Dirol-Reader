package com.therxmv.otaupdates.data.source.remote

import com.therxmv.common.GithubRepo.GITHUB_REPO
import com.therxmv.common.GithubRepo.GITHUB_USERNAME
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LatestReleaseRemoteDataSource @Inject constructor(
    private val apiService: GithubApiService,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun getLatestRelease() = withContext(ioDispatcher) {
        try {
            apiService.getLatestRelease(GITHUB_USERNAME, GITHUB_REPO)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}