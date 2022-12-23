package com.dart69.dartnews.news.data.networking

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import com.dart69.dartnews.news.domain.networking.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.net.InetAddress
import java.net.UnknownHostException

class DefaultNetworkChecker(
    private val hostAddress: String,
    private val dispatchers: AvailableDispatchers,
) : NetworkChecker {
    override suspend fun isNetworkAvailable(): Boolean = withContext(dispatchers.io) {
        try {
            val address = InetAddress.getByName(hostAddress)
            !address.equals("")
        } catch (exception: UnknownHostException) {
            false
        }
    }
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
        }.flowOn(dispatchers.io)
}

class DefaultNetworkObserver(
    private val connectivityManager: ConnectivityManager,
) : NetworkObserver {

    override fun observeNetworkState(): Flow<NetworkState> = callbackFlow {
        val callback = object : NetworkCallback() {
            private fun sendState(networkState: NetworkState) {
                launch {
                    send(networkState)
                }
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                sendState(NetworkState.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                sendState(NetworkState.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                sendState(NetworkState.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                sendState(NetworkState.Unavailable)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

}