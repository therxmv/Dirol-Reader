package com.therxmv.otaupdates.domain.repository

import com.therxmv.otaupdates.data.models.LatestReleaseJson

interface LatestReleaseRepository {
    suspend fun getLatestRelease(): LatestReleaseJson?
}