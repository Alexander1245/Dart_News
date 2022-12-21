package com.dart69.dartnews.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.dart69.dartnews.ui.values.Dimens

@OptIn(ExperimentalMaterial3Api::class)
object NewsTopAppBarDefaults {

    @Composable
    fun topAppBarColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        titleContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        navigationIconColor: Color = MaterialTheme.colorScheme.onPrimary,
    ): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        navigationIconContentColor = navigationIconColor
    )
}

object NewsCardDefaults {
    @Composable
    fun cardColors(
        containerColor: Color = MaterialTheme.colorScheme.secondary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary
    ): CardColors =
        CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
}

object NewsListItemDefaults {
    fun paddingValues(): PaddingValues = PaddingValues(
        start = Dimens.MediumPadding,
        end = Dimens.SmallPadding,
        top = Dimens.MediumPadding,
        bottom = Dimens.SmallPadding
    )
}