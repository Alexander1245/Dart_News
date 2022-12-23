package com.dart69.dartnews.news.di

import android.content.Context
import android.net.ConnectivityManager
import com.dart69.dartnews.news.data.networking.DefaultConnectionObserver
import com.dart69.dartnews.news.data.networking.DefaultNetworkChecker
import com.dart69.dartnews.news.data.networking.DefaultNetworkObserver
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import com.dart69.dartnews.news.domain.networking.ConnectionObserver
import com.dart69.dartnews.news.domain.networking.NetworkChecker
import com.dart69.dartnews.news.domain.networking.NetworkObserver
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
}