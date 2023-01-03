package com.dart69.dartnews.news.networking

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NetworkState {
    Available, Unavailable, Losing, Lost
}

interface NetworkObserver {
    fun observeNetworkState(): Flow<NetworkState>
}

class DefaultNetworkObserver(
    private val connectivityManager: ConnectivityManager,
    private val initialTimeout: Long = 5000L,
) : NetworkObserver {

    override fun observeNetworkState(): Flow<NetworkState> = callbackFlow {
        val initialState = launch {
            delay(initialTimeout)
            send(NetworkState.Unavailable)
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            private fun sendAndCancelInitial(networkState: NetworkState) {
                launch {
                    if (initialState.isActive) initialState.cancel()
                    send(networkState)
                }
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                sendAndCancelInitial(NetworkState.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                sendAndCancelInitial(NetworkState.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                sendAndCancelInitial(NetworkState.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                sendAndCancelInitial(NetworkState.Unavailable)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

}