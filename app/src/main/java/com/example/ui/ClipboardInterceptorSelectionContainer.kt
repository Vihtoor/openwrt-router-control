package com.example.ui

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

@Composable
fun ClipboardInterceptorSelectionContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val originalClipboard = LocalClipboardManager.current
    val customClipboard = remember(originalClipboard) {
        object : ClipboardManager {
            override fun getText(): AnnotatedString? = originalClipboard.getText()
            override fun setText(annotatedString: AnnotatedString) {
                originalClipboard.setText(AnnotatedString(annotatedString.text.replace("\u200B", "")))
            }
            override fun hasText(): Boolean = originalClipboard.hasText()
        }
    }
    CompositionLocalProvider(
        LocalClipboardManager provides customClipboard
    ) {
        SelectionContainer(modifier = modifier, content = content)
    }
}
