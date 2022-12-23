package com.dart69.test

import com.dart69.dartnews.news.data.DefaultArticlesRepository
import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.datasources.MostViewedApi
import com.dart69.dartnews.news.di.NewsSingletonModule
import com.dart69.dartnews.news.domain.model.Period
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun main() = runBlocking<Unit> {
    val (baseUrl, apiKey) = NewsSingletonModule.provideBaseUrl() to NewsSingletonModule.provideApiKey()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    val dispatchers = NewsSingletonModule.provideDispatchers()
    val mostViewedApi = retrofit.create(MostViewedApi::class.java)
    val remote = ArticlesRemoteDataSource.Default(mostViewedApi, apiKey, dispatchers)
    val cached = ArticlesCachedDataSource.Default()
    val repository = DefaultArticlesRepository(remote, cached, dispatchers)
    repository.observe(Period.Day).collect { results ->
        println(results)
    }
}