package com.dart69.dartnews.news.networking

import com.dart69.dartnews.news.di.HostAddress
import com.dart69.dartnews.news.di.AvailableDispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException
import javax.inject.Inject

interface ConnectionChecker {
    suspend fun isConnected(): Boolean
}

class DefaultConnectionChecker @Inject constructor(
    @HostAddress private val hostAddress: String,
    private val dispatchers: AvailableDispatchers,
) : ConnectionChecker {
    override suspend fun isConnected(): Boolean = withContext(dispatchers.io) {
        try {
            val address = InetAddress.getByName(hostAddress)
            !address.equals("")
        } catch (exception: UnknownHostException) {
            false
        }
    }
}