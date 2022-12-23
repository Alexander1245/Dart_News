package com.dart69.dartnews.news.domain.networking

import kotlinx.coroutines.flow.Flow

enum class NetworkState {
    Available, Unavailable, Losing, Lost
}

enum class ConnectionState {
    Connected, Disconnected
}

interface NetworkObserver {
    fun observeNetworkState(): Flow<NetworkState>
}

interface ConnectionObserver {
    fun observeConnectionState(): Flow<ConnectionState>
}

interface NetworkChecker {
    suspend fun isNetworkAvailable(): Boolean
}