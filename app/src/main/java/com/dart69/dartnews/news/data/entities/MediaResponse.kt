package com.dart69.dartnews.news.data.entities

data class MediaResponse(
    val approved_for_syndication: Int,
    val caption: String,
    val copyright: String,
    val `media-metadata`: List<MediaMetadataResponse>,
    val subtype: String,
    val type: String
)