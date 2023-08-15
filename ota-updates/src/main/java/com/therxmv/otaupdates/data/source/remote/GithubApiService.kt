package com.therxmv.otaupdates.data.source.remote

import com.therxmv.otaupdates.data.models.LatestReleaseModel
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {
    @GET("repos/{username}/{repoName}/releases/latest")
    suspend fun getLatestRelease(
        @Path("username") username: String,
        @Path("repoName") repoName: String,
    ): LatestReleaseModel
}