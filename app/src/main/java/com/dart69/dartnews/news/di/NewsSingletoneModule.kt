package com.dart69.dartnews.news.di

import com.dart69.dartnews.news.data.DefaultArticlesRepository
import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.datasources.MostViewedApi
import com.dart69.dartnews.news.domain.model.AppDispatchers
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
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
    fun provideMostViewedApi(retrofit: Retrofit): MostViewedApi = retrofit.create()

    @Provides
    @Singleton
    fun provideArticlesRemoteDataSource(
        mostViewedApi: MostViewedApi,
        @ApiKey apiKey: String,
        dispatchers: AvailableDispatchers
    ): ArticlesRemoteDataSource =
        ArticlesRemoteDataSource.Default(
            mostViewedApi,
            apiKey,
            dispatchers
        )

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