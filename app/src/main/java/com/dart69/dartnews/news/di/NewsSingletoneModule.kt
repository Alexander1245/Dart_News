package com.dart69.dartnews.news.di

import com.dart69.dartnews.news.data.DefaultArticlesRepository
import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.datasources.MostPopularApi
import com.dart69.dartnews.news.data.datasources.ResponseFactory
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.other.AppDispatchers
import com.dart69.dartnews.news.other.AvailableDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HostAddress

@Module
@InstallIn(SingletonComponent::class)
object NewsSingletonModule {

    @Provides
    @HostAddress
    fun provideHostAddress(): String = "www.nytimes.com"

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = "https://api.nytimes.com"

    @Provides
    @ApiKey
    fun provideApiKey(): String = "YjSGmygj9kTfxnuqYiKkxmMHoEbRuuya"

    @Provides
    fun provideDispatchers(): AvailableDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideRetrofit(@BaseUrl baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideMostPopularApi(retrofit: Retrofit): MostPopularApi = retrofit.create()

    @Provides
    @Singleton
    fun provideResponseFactory(
        @ApiKey apiKey: String,
        mostPopularApi: MostPopularApi
    ): ResponseFactory = ResponseFactory.Default(apiKey, mostPopularApi)

    @Provides
    @Singleton
    fun provideArticlesRemoteDataSource(
        responseFactory: ResponseFactory,
        dispatchers: AvailableDispatchers
    ): ArticlesRemoteDataSource =
        ArticlesRemoteDataSource.Default(responseFactory, dispatchers)

    @Provides
    @Singleton
    fun provideArticlesCachedDataSource(): ArticlesCachedDataSource =
        ArticlesCachedDataSource.Default()

    @Provides
    @Singleton
    fun provideArticlesRepository(
        remoteDataSource: ArticlesRemoteDataSource,
        cachedDataSource: ArticlesCachedDataSource,
        dispatchers: AvailableDispatchers,
    ): ArticlesRepository =
        DefaultArticlesRepository(
            remoteDataSource,
            cachedDataSource,
            dispatchers
        )
}