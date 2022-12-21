package com.dart69.dartnews.news.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

typealias ResultsFlow<T> = Flow<Results<T>>

sealed class Results<T> {
    class Loading<T> : Results<T>()

    data class Error<T>(val exception: Throwable) : Results<T>()

    data class Completed<T>(val data: T) : Results<T>()
}

fun <T, R> Results<T>.mapResults(onCompleted: (T) -> R): Results<R> = when (this) {
    is Results.Loading -> Results.Loading()
    is Results.Error -> Results.Error(exception)
    is Results.Completed -> Results.Completed(onCompleted(data))
}

fun <T> resultsFlowOf(block: suspend () -> T): ResultsFlow<T> = flow<Results<T>> {
    emit(Results.Completed(block()))
}.onStart {
    emit(Results.Loading())
}.catch { throwable ->
    emit(Results.Error(throwable))
}