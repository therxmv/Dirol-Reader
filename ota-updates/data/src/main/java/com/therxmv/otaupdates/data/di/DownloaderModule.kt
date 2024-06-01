package com.therxmv.otaupdates.data.di

import com.therxmv.otaupdates.data.source.remote.LatestReleaseDownloader
import com.therxmv.otaupdates.domain.repository.DownloaderApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloaderModule {

    @Binds
    @Singleton
    abstract fun bindsDownloader(downloader: LatestReleaseDownloader): DownloaderApi
}