package com.dart69.dartnews.news.networking

import com.dart69.dartnews.news.domain.model.Results
import com.dart69.dartnews.news.domain.model.ResultsFlow
import kotlinx.coroutines.flow.flow

class ConnectionRefusedException(message: String? = null) : Exception(message)

fun <T> connectionErrorResults(): ResultsFlow<T> = flow {
    emit(Results.Error(ConnectionRefusedException()))
}