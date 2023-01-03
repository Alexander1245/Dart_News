package com.dart69.dartnews.news.networking

import com.dart69.dartnews.news.other.AvailableDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

enum class ConnectionState {
    Connected, Disconnected
}

interface ConnectionObserver {
    fun observeConnectionState(): Flow<ConnectionState>
}

class DefaultConnectionObserver(
    private val networkChecker: NetworkChecker,
    private val networkObserver: NetworkObserver,
    private val dispatchers: AvailableDispatchers,
) : ConnectionObserver {
    override fun observeConnectionState(): Flow<ConnectionState> =
        networkObserver.observeNetworkState().map { networkState ->
            if (networkState == NetworkState.Available && networkChecker.isNetworkAvailable()) {
                ConnectionState.Connected
            } else {
                ConnectionState.Disconnected
            }
        }.distinctUntilChanged().flowOn(dispatchers.io)
}