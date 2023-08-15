package com.therxmv.otaupdates.data.repository

import com.therxmv.otaupdates.data.models.LatestReleaseModel

interface LatestReleaseRepository {
    suspend fun getLatestRelease(): LatestReleaseModel?
}