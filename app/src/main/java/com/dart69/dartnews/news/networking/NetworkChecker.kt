package com.dart69.dartnews.news.networking

import com.dart69.dartnews.news.other.AvailableDispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException

interface NetworkChecker {
    suspend fun isNetworkAvailable(): Boolean
}

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