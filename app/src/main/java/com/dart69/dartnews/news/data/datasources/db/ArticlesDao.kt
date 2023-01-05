package com.dart69.dartnews.news.data.datasources.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dart69.dartnews.news.data.entities.local.ArticleEntity

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM ArticleEntity")
    suspend fun loadAll(): List<ArticleEntity>?

    @Insert
    suspend fun insert(entity: ArticleEntity)

    @Delete
    suspend fun clear(entity: ArticleEntity)
}