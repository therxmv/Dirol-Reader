package com.therxmv.dirolreader.di.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        RemoteDataSourceModule::class,
        LocalDataSourceModule::class,
        RepositoryModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
class DataModule