package com.dart69.dartnews.news.domain.model

import com.dart69.dartnews.news.networking.ConnectionRefusedException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

typealias ResultsFlow<T> = Flow<Results<T>>

sealed class Results<T> {
    class Loading<T> : Results<T>()

    data class Error<T>(val throwable: Throwable) : Results<T>()

    data class Completed<T>(val data: T) : Results<T>()
}

fun <T> Results<T>.isConnectionError(): Boolean =
    this is Results.Error && throwable is ConnectionRefusedException

suspend fun <T, R> Results<T>.mapResults(onCompleted: suspend (T) -> R): Results<R> = when (this) {
    is Results.Loading -> Results.Loading()
    is Results.Error -> Results.Error(throwable)
    is Results.Completed -> Results.Completed(onCompleted(data))
}

fun <T> resultsFlowOf(block: suspend () -> T): ResultsFlow<T> = flow<Results<T>> {
    emit(Results.Completed(block()))
}.onStart {
    emit(Results.Loading())
}.catch { throwable ->
    emit(Results.Error(throwable))
}

fun <T> Results<T>.takeCompleted(): T? = if (this is Results.Completed) data else null

@OptIn(FlowPreview::class)
fun <T1, T2, R> Flow<T1>.combineFlatten(
    other: Flow<T2>,
    concurrency: Int = DEFAULT_CONCURRENCY,
    transform: suspend (T1, T2) -> Flow<R>,

    ): Flow<R> {
    return this.combine(other) { data1, data2 ->
        transform(data1, data2)
    }.flattenMerge(concurrency)
}