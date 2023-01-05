package com.dart69.dartnews.news.di

import com.dart69.dartnews.news.domain.usecase.FetchArticlesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface NewsModule {

    @Binds
    fun bindFetchArticlesUseCase(
        implementation: FetchArticlesUseCase.Default
    ): FetchArticlesUseCase
}