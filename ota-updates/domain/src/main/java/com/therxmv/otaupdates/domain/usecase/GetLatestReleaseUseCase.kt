package com.therxmv.otaupdates.domain.usecase

import com.therxmv.otaupdates.domain.repository.LatestReleaseRepository
import javax.inject.Inject

class GetLatestReleaseUseCase @Inject constructor(
    private val latestReleaseRepository: LatestReleaseRepository,
) {
    suspend operator fun invoke() = latestReleaseRepository.getLatestRelease()
}