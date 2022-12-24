package com.dart69.dartnews.news.domain.model

interface MenuItem

data class TextMenuItem(val text: String, val onClick: () -> Unit) : MenuItem

object DividerItem : MenuItem