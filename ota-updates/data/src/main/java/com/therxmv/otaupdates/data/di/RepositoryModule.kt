package com.therxmv.otaupdates.data.di

import com.therxmv.otaupdates.data.repository.LatestReleaseRepositoryImpl
import com.therxmv.otaupdates.domain.repository.LatestReleaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsLatestReleaseRepository(repository: LatestReleaseRepositoryImpl): LatestReleaseRepository
}