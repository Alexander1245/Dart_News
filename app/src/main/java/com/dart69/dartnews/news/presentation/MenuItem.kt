package com.dart69.dartnews.news.presentation

interface MenuItem

data class TextItem(val text: String): MenuItem

data class ClickableTextItem(val text: String, val onClick: () -> Unit) : MenuItem

object DividerItem : MenuItem