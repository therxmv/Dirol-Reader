package com.therxmv.otaupdates.di

import com.therxmv.constants.GithubRepo.BASE_URL
import com.therxmv.otaupdates.data.source.remote.GithubApiService
import com.therxmv.otaupdates.downloadmanager.DownloaderModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(
    includes = [
        RepositoryModule::class,
        DownloaderModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
class OtaModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService =
        retrofit.create(GithubApiService::class.java)
}