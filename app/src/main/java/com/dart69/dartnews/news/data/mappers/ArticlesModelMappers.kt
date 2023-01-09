package com.dart69.dartnews.news.data.mappers

import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.data.entities.local.ArticleEntity
import com.dart69.dartnews.news.domain.model.Article
import javax.inject.Inject

interface ArticleResponseMapper : ModelMapper<ArticleResponse, Article> {

    class Default @Inject constructor() : ArticleResponseMapper {
        override fun toModel(rawModel: ArticleResponse): Article = rawModel.run {
            Article(
                id = id,
                title = title,
                content = abstract,
                titleImageUrl = media.firstOrNull()?.`media-metadata`
                    ?.maxBy {
                        it.height * it.width
                    }?.url.orEmpty(),
                sourceUrl = url,
                byLine = byline,
                publishedDate = published_date,
            )
        }
    }
}

interface ArticleEntityMapper : BidirectionalModelMapper<ArticleEntity, Article> {

    class Default @Inject constructor(): ArticleEntityMapper {
        override fun toModel(rawModel: ArticleEntity): Article = rawModel.run {
            Article(
                id = id,
                title = title,
                content = content,
                titleImageUrl = titleImageUrl,
                sourceUrl = sourceUrl,
                byLine = byLine,
                publishedDate = publishedDate,
            )
        }

        override fun toRawModel(model: Article): ArticleEntity = model.run {
            ArticleEntity(
                id = id,
                title = title,
                content = content,
                titleImageUrl = titleImageUrl,
                sourceUrl = sourceUrl,
                byLine = byLine,
                publishedDate = publishedDate,
            )
        }
    }
}