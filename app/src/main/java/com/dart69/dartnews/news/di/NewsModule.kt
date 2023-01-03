package com.dart69.dartnews.news.di

import android.content.Context
import android.net.ConnectivityManager
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.domain.usecase.FetchArticlesUseCase
import com.dart69.dartnews.news.networking.*
import com.dart69.dartnews.news.other.AvailableDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object NewsModule {

    @Provides
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver =
        DefaultNetworkObserver(context.getSystemService(ConnectivityManager::class.java))

    @Provides
    fun provideNetworkChecker(
        @HostAddress hostAddress: String,
        dispatchers: AvailableDispatchers
    ): NetworkChecker = DefaultNetworkChecker(hostAddress, dispatchers)

    @Provides
    fun provideConnectionObserver(
        networkChecker: NetworkChecker,
        networkObserver: NetworkObserver,
        dispatchers: AvailableDispatchers
    ): ConnectionObserver = DefaultConnectionObserver(
        networkChecker, networkObserver, dispatchers
    )

    @Provides
    fun provideFetchArticlesUseCase(
        repository: ArticlesRepository,
        connectionObserver: ConnectionObserver
    ): FetchArticlesUseCase = FetchArticlesUseCase.Default(repository, connectionObserver)
}