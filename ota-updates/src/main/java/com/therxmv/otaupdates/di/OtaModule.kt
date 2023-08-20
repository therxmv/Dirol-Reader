package com.therxmv.otaupdates.di

import android.content.Context
import com.therxmv.constants.GithubRepo.BASE_URL
import com.therxmv.otaupdates.domain.repository.LatestReleaseRepository
import com.therxmv.otaupdates.data.repository.LatestReleaseRepositoryImpl
import com.therxmv.otaupdates.data.source.remote.GithubApiService
import com.therxmv.otaupdates.data.source.remote.LatestReleaseRemoteDataSource
import com.therxmv.otaupdates.domain.usecase.GetLatestReleaseUseCase
import com.therxmv.otaupdates.downloadmanager.Downloader
import com.therxmv.otaupdates.downloadmanager.LatestReleaseDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OtaModule {
    @Provides
    @Singleton
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit) = retrofit.create(GithubApiService::class.java)

    @Provides
    @Singleton
    fun provideLatestReleaseRemoteDataSource(githubApiService: GithubApiService): LatestReleaseRemoteDataSource {
        return LatestReleaseRemoteDataSource(githubApiService)
    }

    @Provides
    @Singleton
    fun provideLatestReleaseRepositoryImpl(latestReleaseRemoteDataSource: LatestReleaseRemoteDataSource): LatestReleaseRepository {
        return LatestReleaseRepositoryImpl(latestReleaseRemoteDataSource)
    }

    @Provides
    fun provideLatestReleaseDownloader(@ApplicationContext context: Context): Downloader {
        return LatestReleaseDownloader(context)
    }

    @Provides
    fun provideGetLatestReleaseUseCase(latestReleaseRepository: LatestReleaseRepository): GetLatestReleaseUseCase {
        return GetLatestReleaseUseCase(latestReleaseRepository)
    }
}