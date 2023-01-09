package com.dart69.dartnews.news.data.datasources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dart69.dartnews.news.data.entities.local.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1
)
abstract class ArticlesDataBase : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}