package com.therxmv.dirolreader.di

import com.therxmv.dirolreader.BuildConfig
import com.therxmv.dirolreader.di.data.ClientModule
import com.therxmv.dirolreader.di.data.LocalDataSourceModule
import com.therxmv.dirolreader.di.data.RemoteDataSourceModule
import com.therxmv.dirolreader.di.data.RepositoryModule
import com.therxmv.otaupdates.data.di.OtaModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [
        RemoteDataSourceModule::class,
        LocalDataSourceModule::class,
        RepositoryModule::class,
        OtaModule::class,
        ClientModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    @Named("VersionCode")
    fun providesVersionCode(): Int = BuildConfig.VERSION_CODE
}