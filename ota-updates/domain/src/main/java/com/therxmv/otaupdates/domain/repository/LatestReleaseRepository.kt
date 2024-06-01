package com.therxmv.otaupdates.domain.repository

import com.therxmv.otaupdates.domain.models.LatestReleaseModel

interface LatestReleaseRepository {
    suspend fun getLatestRelease(): LatestReleaseModel?
}