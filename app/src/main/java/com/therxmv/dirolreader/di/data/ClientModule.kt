package com.therxmv.dirolreader.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

    @Singleton
    @Provides
    fun provideClient(): Client =
        Client.create(
            /* updateHandler = */ {},
            /* updateExceptionHandler = */ null,
            /* defaultExceptionHandler = */ null
        ).also {
            it.send(TdApi.SetLogVerbosityLevel(0)) {}
        }
}