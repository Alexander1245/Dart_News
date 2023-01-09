package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.news.presentation.ClickableTextItem
import com.dart69.dartnews.news.presentation.DividerItem
import com.dart69.dartnews.news.presentation.MenuItem
import com.dart69.dartnews.news.presentation.ui.Button.Companion.createButton
import kotlin.reflect.KClass

sealed class Button(
    val isEnabled: Boolean,
    protected val content: @Composable () -> Unit,
    val onClick: () -> Unit,
) {
    @Composable
    abstract fun Draw()

    class Icon(
        isEnabled: Boolean,
        content: @Composable () -> Unit,
        onClick: () -> Unit
    ) : Button(isEnabled, content, onClick) {
        @Composable
        override fun Draw() {
            IconButton(onClick = onClick, content = content, enabled = isEnabled)
        }
    }

    class Regular(
        isEnabled: Boolean,
        content: @Composable () -> Unit,
        onClick: () -> Unit
    ) : Button(isEnabled, content, onClick) {

        @Composable
        override fun Draw() {
            Button(onClick = onClick, content = { content() }, enabled = isEnabled, colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.primary,
            ))
        }
    }

    companion object {
        fun <T : Button> KClass<T>.createButton(
            isEnabled: Boolean,
            content: @Composable () -> Unit,
            onClick: () -> Unit
        ): T {
            val button = when (this) {
                Icon::class -> Icon(isEnabled, content, onClick)
                Regular::class -> Regular(isEnabled, content, onClick)
                else -> error("Invalid modelClass.")
            }
            return button as T
        }
    }
}

@Composable
fun NewsDropdownMenu(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    items: List<MenuItem>,
    content: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.more)
        )
    },
    buttonClass: KClass<out Button> = Button.Regular::class,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        buttonClass.createButton(isEnabled, content, onClick = { expanded = true }).Draw()
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                MenuItem(menuItem = it, onTouchUp = { expanded = false })
            }
        }
    }
}

@Preview
@Composable
fun NewsDropdownMenuPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {

        NewsDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            items = listOf(
                ClickableTextItem("Delete") {},
                ClickableTextItem("Download") {},
                DividerItem,
                ClickableTextItem("Close") {},
            ),
        )
    }
}