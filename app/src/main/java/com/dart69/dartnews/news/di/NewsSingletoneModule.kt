package com.dart69.dartnews.news.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.dart69.dartnews.news.data.ArticlesCache
import com.dart69.dartnews.news.data.DefaultArticlesRepository
import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.datasources.MostPopularApi
import com.dart69.dartnews.news.data.datasources.ResponseFactory
import com.dart69.dartnews.news.data.datasources.db.ArticlesDao
import com.dart69.dartnews.news.data.datasources.db.ArticlesDataBase
import com.dart69.dartnews.news.data.mappers.ArticleEntityMapper
import com.dart69.dartnews.news.data.mappers.ArticleResponseMapper
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.networking.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
interface NewsSingletonModule {

    @Binds
    fun bindResponseFactory(
        factory: ResponseFactory.Default
    ): ResponseFactory

    @Binds
    fun bindNetworkChecker(
        connectionChecker: DefaultConnectionChecker
    ): ConnectionChecker

    @Binds
    @Singleton
    fun bindConnectionObserver(
        connectionObserver: DefaultConnectionObserver
    ): ConnectionObserver

    @Binds
    @Singleton
    fun bindArticlesRemoteDataSource(
        remoteDataSource: ArticlesRemoteDataSource.Default
    ): ArticlesRemoteDataSource

    @Binds
    @Singleton
    fun bindCache(
        cache: ArticlesCache.Default
    ): ArticlesCache

    @Binds
    @Singleton
    fun bindArticlesCachedDataSource(
        dataSource: ArticlesCachedDataSource.Default
    ): ArticlesCachedDataSource

    @Binds
    @Singleton
    fun bindArticlesRepository(
        repository: DefaultArticlesRepository
    ): ArticlesRepository

    @Binds
    fun bindDispatchers(
        dispatchers: AppDispatchers
    ): AvailableDispatchers

    @Binds
    fun bindResponseMapper(
        mapper: ArticleResponseMapper.Default
    ): ArticleResponseMapper

    @Binds
    fun bindEntityMapper(
        mapper: ArticleEntityMapper.Default
    ): ArticleEntityMapper
}

@Module
@InstallIn(SingletonComponent::class)
object NewsSingletonProviders {

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
    @Singleton
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver =
        DefaultNetworkObserver(context.getSystemService(ConnectivityManager::class.java))

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
    fun provideDataBase(@ApplicationContext context: Context): ArticlesDataBase =
        Room.databaseBuilder(
            context,
            ArticlesDataBase::class.java,
            ArticlesDataBase::class.simpleName!!
        ).build()

    @Provides
    @Singleton
    fun provideArticlesDao(dataBase: ArticlesDataBase): ArticlesDao =
        dataBase.articlesDao()

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope = MainScope()
}