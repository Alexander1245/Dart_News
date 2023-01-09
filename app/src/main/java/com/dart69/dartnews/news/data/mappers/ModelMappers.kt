package com.dart69.dartnews.news.data.mappers

import com.dart69.dartnews.news.domain.model.Model

interface RawModel

fun interface ModelMapper<I : RawModel, O : Model> {
    fun toModel(rawModel: I): O
}

fun interface RawModelMapper<I : RawModel, O : Model> {
    fun toRawModel(model: O): I
}

interface BidirectionalModelMapper<I : RawModel, O : Model> : ModelMapper<I, O>, RawModelMapper<I, O>